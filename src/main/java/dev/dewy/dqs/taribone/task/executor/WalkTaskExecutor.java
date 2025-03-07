package dev.dewy.dqs.taribone.task.executor;

/*
 Copyright (c) 2013, DarkStorm (darkstorm@evilminecraft.net)
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.taribone.entity.player.TariboneDQSPlayer;
import dev.dewy.dqs.taribone.pathing.PathNode;
import dev.dewy.dqs.taribone.task.api.TaskStatus;
import dev.dewy.dqs.taribone.world.block.Block;
import dev.dewy.dqs.taribone.world.block.Material;
import dev.dewy.dqs.utils.vector.Vector3d;
import dev.dewy.dqs.utils.vector.Vector3i;

import java.util.concurrent.*;

public class WalkTaskExecutor extends AbstractTaskExecutor
{

    private static double speed = 0.15, jumpFactor = 3, fallFactor = 4, liquidFactor = 0.5;
    private static int defaultTimeout = 6000;

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final Vector3i target;

    private final long startTime;
    private final Callable<PathNode> task = new Callable<PathNode>()
    {
        @Override
        public PathNode call() throws Exception
        {
            TariboneDQSPlayer player = dqs.getPlayer();
            PathNode start = dqs.getPathFinder().search(player.getLocation().intVector(), target);
            return start;
        }
    };
    private Future<PathNode> pathFuture;
    private PathNode nextStep;
    private int ticksSinceStepChange = 0;
    private int timeout = defaultTimeout;

    public WalkTaskExecutor(final DQS dqs, final Vector3i target)
    {
        super(dqs);
        this.target = target;

        if (dqs.getPlayer().getLocation().intVector().equals(target))
        {
//            Constants.TARIBONE_LOG.warn("Useless walk task. Bot and given target location equal.");
        }
        pathFuture = service.submit(task);
        startTime = System.currentTimeMillis();
    }

    public static double getDefaultSpeed()
    {
        return speed;
    }

    public static void setDefaultSpeed(double defaultSpeed)
    {
        WalkTaskExecutor.speed = defaultSpeed;
    }

    public static double getDefaultJumpFactor()
    {
        return jumpFactor;
    }

    public static void setDefaultJumpFactor(double defaultJumpFactor)
    {
        WalkTaskExecutor.jumpFactor = defaultJumpFactor;
    }

    public static double getDefaultFallFactor()
    {
        return fallFactor;
    }

    public static void setDefaultFallFactor(double defaultFallFactor)
    {
        WalkTaskExecutor.fallFactor = defaultFallFactor;
    }

    public static double getDefaultLiquidFactor()
    {
        return liquidFactor;
    }

    public static void setDefaultLiquidFactor(double defaultLiquidFactor)
    {
        WalkTaskExecutor.liquidFactor = defaultLiquidFactor;
    }

    public static int getDefaultTimeout()
    {
        return defaultTimeout;
    }

    public static void setDefaultTimeout(int defaultTimeout)
    {
        WalkTaskExecutor.defaultTimeout = defaultTimeout;
    }

    @Override
    protected TaskStatus onTick()
    {

        if (pathFuture != null && !pathFuture.isDone())
        {
            // If we're still calculating the path:
            // Check timeout
            if (timeout > 0 && System.currentTimeMillis() - startTime > timeout)
            {
                pathFuture.cancel(true);
                nextStep = null;
                return TaskStatus.forFailure(String.format("Path search from %s to %s timed out (%s ms)", dqs.getPlayer().getLocation(), target, timeout));
            } else
            {
                return TaskStatus.forInProgress();
            }

        } else if (pathFuture != null && pathFuture.isDone() && !pathFuture.isCancelled())
        {
            // If we've found a path successfully
            try
            {
                nextStep = pathFuture.get();
                ticksSinceStepChange = 0;
            } catch (InterruptedException e)
            {
                return TaskStatus.forFailure(e.getMessage(), e);
            } catch (ExecutionException e)
            {
                /*
                if (e.getCause() instanceof ChunkNotLoadedException) {
                    return status = TaskStatus.forInProgress();
                }*/
                return TaskStatus.forFailure(e.getMessage(), e.getCause());
            } finally
            {
                pathFuture = null;
            }
        }

        // If we have no more steps to do, we're done
        if (nextStep == null)
        {
            return TaskStatus.forSuccess();
        }

        TariboneDQSPlayer player = dqs.getPlayer();

        // Skip the step if the next step is close by
        if (nextStep.getNext() != null && player.getLocation().distanceSquared(nextStep.getNext().getLocation().doubleVector()) < 0.05)
        {
            nextStep = nextStep.getNext();
            ticksSinceStepChange = 0;
        }

        // If the player is too far away from the next step
        // Abort for now
        if (player.getLocation().distanceSquared(nextStep.getLocation().doubleVector()) > 5.0)
        {
//            Constants.TARIBONE_LOG.info(String.format("Strayed from path. %s -> %s", player.getLocation(), nextStep.getLocation()));
            // TODO: Fix later
            //TaskStatus status = TaskStatus.forInProgress();
            //pathFuture = service.submit(task);
            return TaskStatus.forFailure("Strayed from path. %s -> %s");
        }

        // Keep track of how many ticks a step takes
        // If a step takes too many ticks, abort
        ticksSinceStepChange++;
        if (ticksSinceStepChange > 80)
        {
            nextStep = null;
            return TaskStatus.forFailure("Too many ticks since step change");
        }

        // Get locations
        Vector3d moveLoc = player.getLocation();
        Vector3i blockLoc = moveLoc.intVector().add(new Vector3i(0, -1, 0));
        Block thisBlock;

        try
        {
            thisBlock = dqs.getWorld().getBlockAt(blockLoc);
        } catch (ChunkNotLoadedException e)
        {
            // TODO: Fix: Wait until chunk is loaded.
//            Constants.TARIBONE_LOG.warn(String.format("Block under player: %s", blockLoc));
//            Constants.TARIBONE_LOG.warn(String.format("Player at %s", moveLoc));
            return TaskStatus.forFailure(e.getMessage());
        }

        // Step
        Vector3i stepTargetBlock = nextStep.getLocation();
        if (stepTargetBlock == null)
        {
            return TaskStatus.forFailure("No next step");
        }
        Vector3d stepTarget = stepTargetBlock.doubleVector();

        // Stand on the center of a block
        stepTarget = stepTarget.add(0.5, 0, 0.5);

        // Calculate speed
        double moveSpeed = speed;
        boolean inLiquid = false; // TODO: player.isInLiquid();
        if (Material.getById(thisBlock.getTypeId()) == Material.SOUL_SAND)
        {
            if (Material.getById(thisBlock.getTypeId()) == Material.SOUL_SAND)
            {
                // Soulsand makes us shorter 8D
                stepTarget = stepTarget.add(0, -0.12, 0);
            }
            moveSpeed *= liquidFactor;
        } else if (inLiquid)
        {
            moveSpeed *= liquidFactor;
        }

        double stepX = stepTarget.getX(), stepY = stepTarget.getY(), stepZ = stepTarget.getZ();

        // See if we're climbing, or jumping
        if (moveLoc.getY() != stepY)
        {
            boolean canClimbBlock = false;
            try
            {
                canClimbBlock = dqs.getPathFinder().getWorldPhysics().canClimb(moveLoc.intVector());
            } catch (ChunkNotLoadedException e)
            {
                return TaskStatus.forInProgress();
            }
            if (!inLiquid && !canClimbBlock)
            {
                if (moveLoc.getY() < stepY)
                {
                    moveSpeed *= jumpFactor;
                } else
                {
                    moveSpeed *= fallFactor;
                }
            }

            // Set new Y-coord
            double offsetY = moveLoc.getY() < stepY ? Math.min(moveSpeed, stepY - moveLoc.getY()) : Math.max(-moveSpeed, stepY - moveLoc.getY());
            moveLoc = moveLoc.add(new Vector3d(0, offsetY, 0));
        }

        if (moveLoc.getX() != stepX)
        {
            double offsetX = moveLoc.getX() < stepX ? Math.min(moveSpeed, stepX - moveLoc.getX()) : Math.max(-moveSpeed, stepX - moveLoc.getX());
            moveLoc = moveLoc.add(new Vector3d(offsetX, 0, 0));
        }

        if (moveLoc.getZ() != stepZ)
        {
            double offsetZ = moveLoc.getZ() < stepZ ? Math.min(moveSpeed, stepZ - moveLoc.getZ()) : Math.max(-moveSpeed, stepZ - moveLoc.getZ());
            moveLoc = moveLoc.add(new Vector3d(0, 0, offsetZ));
        }

        // Send new player location to server
        dqs.getController().updateLocation(moveLoc);

        if (moveLoc.equals(stepTarget))
        {
            nextStep = nextStep.getNext();
            ticksSinceStepChange = 0;
        }

        return TaskStatus.forInProgress();
    }

    @Override
    protected void onStop()
    {
        if (pathFuture != null && !pathFuture.isDone())
        {
            pathFuture.cancel(true);
        }
        nextStep = null;
        this.service.shutdown();
    }

    public Vector3i getTarget()
    {
        return target;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Walk speed, in blocks/tick. Default is 0.15.
     */
    public double getSpeed()
    {
        return speed;
    }

    /**
     * Set walk speed.
     *
     * @param speed Walk speed, in blocks/tick. Default is 0.15.
     */
    public void setSpeed(double speed)
    {
        WalkTaskExecutor.speed = speed;
    }

    public double getJumpFactor()
    {
        return jumpFactor;
    }

    public void setJumpFactor(double jumpFactor)
    {
        WalkTaskExecutor.jumpFactor = jumpFactor;
    }

    public double getFallFactor()
    {
        return fallFactor;
    }

    public void setFallFactor(double fallFactor)
    {
        WalkTaskExecutor.fallFactor = fallFactor;
    }

    public double getLiquidFactor()
    {
        return liquidFactor;
    }

    public void setLiquidFactor(double liquidFactor)
    {
        WalkTaskExecutor.liquidFactor = liquidFactor;
    }

    public boolean isMoving()
    {
        return nextStep != null;
    }
}
