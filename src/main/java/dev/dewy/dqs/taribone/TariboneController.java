package dev.dewy.dqs.taribone;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.client.player.*;
import dev.dewy.dqs.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.entity.player.Hand;
import dev.dewy.dqs.protocol.game.entity.player.PlayerAction;
import dev.dewy.dqs.taribone.entity.player.TaribonePlayer;
import dev.dewy.dqs.taribone.world.block.Block;
import dev.dewy.dqs.taribone.world.block.BlockFace;
import dev.dewy.dqs.taribone.world.block.Material;
import dev.dewy.dqs.utils.vector.Vector3d;
import dev.dewy.dqs.utils.vector.Vector3i;

/**
 * Represents actions for controlling a Taribone bot.
 */
public class TariboneController
{
    // http://wiki.vg/Inventory
    public static final int PLAYER_INVENTORY_HOTBAR_0 = 36;

    private final DQS dqs;

    /**
     * Creates a new controller.
     *
     * @param dqs the bot.
     */
    public TariboneController(DQS dqs)
    {
        this.dqs = dqs;
    }

    /**
     * Gets the player data for the bot.
     *
     * @return the player data.
     */
    private TaribonePlayer getPlayer()
    {
        return dqs.getPlayer();
    }

    /**
     * Returns the session for the bot.
     *
     * @return the session.
     */
    private Session getSession()
    {
        return dqs.getClient().getSession();
    }

    /**
     * Sends a new location to the Minecraft server.
     *
     * @param vector the location.
     */
    public void updateLocation(Vector3d vector)
    {
        // TODO: fix onGround calculation
        boolean onGround = vector.getY() - Math.floor(vector.getY()) < 0.1;
        getPlayer().setLocation(vector);
        getSession().send(new ClientPlayerPositionPacket(onGround, vector.getX(), vector.getY(), vector.getZ()));
    }

    /**
     * Updates the Minecraft server of the Bot's digging status.
     *
     * @param block the block the bot is digging.
     * @param face  the face the bot is digging.
     * @param state the digging state.
     */
    public void updateDigging(Block block, BlockFace face, DiggingState state)
    {
        Position pos = new Position(block.getX(), block.getY(), block.getZ());
        Packet p;
        switch (state)
        {
            case STARTED_DIGGING:
                p = new ClientPlayerActionPacket(PlayerAction.START_DIGGING, pos, face.getInternalFace());
                break;
            case CANCELLED_DIGGING:
                p = new ClientPlayerActionPacket(PlayerAction.CANCEL_DIGGING, pos, face.getInternalFace());
                break;
            case FINISHED_DIGGING:
                p = new ClientPlayerActionPacket(PlayerAction.FINISH_DIGGING, pos, face.getInternalFace());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported digging state");
        }
        getSession().send(p);
    }

    /**
     * Updates the Minecraft server of block placement.
     *
     * @param block    The block the bot is placing.
     * @param face     The face the bot is placing at.
     * @param hitpoint The hitpoint of the cursor.
     */
    public void placeBlock(Vector3i block, BlockFace face, Vector3d hitpoint)
    {
        // Look at the block
        Vector3d absoluteHit = block.doubleVector().add(0.5, 0.5, 0.5).add(face.getOffset().doubleVector().multiply(0.5));
        double[] yawPitch = calculateYawPitch(getPlayer().getEyeLocation(), absoluteHit);
        getSession().send(new ClientPlayerRotationPacket(true,
                (float) yawPitch[0],
                (float) yawPitch[1]));
        // Swing the arm
        getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
        // Place the block
        getSession().send(new ClientPlayerPlaceBlockPacket(
                // TODO: Figure this out
                new Position(block.getX(), block.getY() - 1, block.getZ()),
                face.getInternalFace(),
                Hand.MAIN_HAND,
                (float) hitpoint.getX(),
                (float) hitpoint.getY(),
                (float) hitpoint.getZ()));
        //bot.getLogger().info("Controller: place  -- block: " + block + ", face: " + face + ", hit: " + hitpoint);
    }

    /**
     * Updates the Minecraft server of a a creative inventory action. In
     * particular, getting a material and setting it as the first slot.
     *
     * @param mat the material to set.
     * @param amt the amount of material to set.
     */
    public void creativeInventoryAction(Material mat, int amt)
    {
        getSession().send(new ClientCreativeInventoryActionPacket(PLAYER_INVENTORY_HOTBAR_0, new ItemStack(mat.getId(), amt)));
    }

    // http://wiki.vg/Protocol#Player_Look
    private double[] calculateYawPitch(Vector3d from, Vector3d to)
    {
        Vector3d dv = to.subtract(from);

        double r = dv.length();

        double yaw = -Math.atan2(dv.getX(), dv.getZ()) / Math.PI * 180;
        if (yaw < 0)
        {
            yaw += 360;
        }
        double pitch = -Math.asin(dv.getY() / r) / Math.PI * 180;
        return new double[] {yaw, pitch};
    }

    /**
     * A state of breaking blocks.
     */
    public enum DiggingState
    {
        STARTED_DIGGING,
        CANCELLED_DIGGING,
        FINISHED_DIGGING
    }
}
