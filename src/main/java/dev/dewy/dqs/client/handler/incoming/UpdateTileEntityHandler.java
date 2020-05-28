package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import dev.dewy.dqs.nbt.tag.builtin.CompoundTag;
import dev.dewy.dqs.nbt.tag.builtin.IntTag;
import net.daporkchop.lib.unsafe.PUnsafe;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class UpdateTileEntityHandler implements HandlerRegistry.IncomingHandler<ServerUpdateTileEntityPacket, DQSClientSession>
{
    protected static final long COLUMN_TILEENTITIES_OFFSET = PUnsafe.pork_getOffset(Column.class, "tileEntities");

    @Override
    public boolean apply(ServerUpdateTileEntityPacket packet, DQSClientSession session)
    {
        Column column = CACHE.getChunkCache().get(packet.getPosition().getX() >> 4, packet.getPosition().getZ() >> 4);
        CompoundTag[] oldArray = column.getTileEntities();
        int index = -1;
        for (int i = oldArray.length - 1; i >= 0; i--)
        {
            if (oldArray[i].<IntTag>get("x").getValue() == packet.getPosition().getX()
                    && oldArray[i].<IntTag>get("y").getValue() == packet.getPosition().getY()
                    && oldArray[i].<IntTag>get("z").getValue() == packet.getPosition().getZ())
            {
                index = i;
                break;
            }
        }
        CompoundTag[] newArray;
        if (packet.getNBT() == null)
        {
            if (index == -1)
            {
                newArray = oldArray;
            } else
            {
                newArray = new CompoundTag[oldArray.length - 1];
                System.arraycopy(oldArray, 0, newArray, 0, index - 1);
                System.arraycopy(oldArray, index + 1, newArray, index, oldArray.length - index - 1);
            }
        } else
        {
            if (index == -1)
            {
                newArray = new CompoundTag[oldArray.length + 1];
                System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
                newArray[oldArray.length] = packet.getNBT();
            } else
            {
                newArray = oldArray;
                newArray[index] = packet.getNBT();
            }
            packet.getNBT().put(new IntTag("x", packet.getPosition().getX()));
            packet.getNBT().put(new IntTag("y", packet.getPosition().getY()));
            packet.getNBT().put(new IntTag("z", packet.getPosition().getZ()));
        }
        PUnsafe.putObject(column, COLUMN_TILEENTITIES_OFFSET, newArray);
        return true;
    }

    @Override
    public Class<ServerUpdateTileEntityPacket> getPacketClass()
    {
        return ServerUpdateTileEntityPacket.class;
    }
}
