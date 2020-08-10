package dev.dewy.dqs.taribone.task;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.taribone.task.api.Task;
import dev.dewy.dqs.taribone.task.api.TaskExecutor;
import dev.dewy.dqs.taribone.task.executor.WalkTaskExecutor;
import dev.dewy.dqs.utils.vector.Vector2i;

public class WalkXZTask implements Task
{
    private Vector2i target;

    public Vector2i getTarget()
    {
        return target;
    }

    public void setTarget(Vector2i target)
    {
        this.target = target;
    }

    @Override
    public TaskExecutor toExecutor(DQS dqs)
    {
        try
        {
            return new WalkTaskExecutor(dqs, target.getHighestWalkTarget(dqs.getWorld()));
        } catch (ChunkNotLoadedException e)
        {
//            Constants.TARIBONE_LOG.warn(e);
            return new WalkTaskExecutor(dqs, dqs.getPlayer().getLocation().intVector());
        }
    }
}
