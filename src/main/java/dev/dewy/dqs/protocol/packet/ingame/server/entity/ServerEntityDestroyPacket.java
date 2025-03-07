package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerEntityDestroyPacket extends MinecraftPacket
{
    private int[] entityIds;

    @SuppressWarnings("unused")
    private ServerEntityDestroyPacket()
    {
    }

    public ServerEntityDestroyPacket(int... entityIds)
    {
        this.entityIds = entityIds;
    }

    public int[] getEntityIds()
    {
        return this.entityIds;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityIds = new int[in.readVarInt()];
        for (int index = 0; index < this.entityIds.length; index++)
        {
            this.entityIds[index] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityIds.length);
        for (int entityId : this.entityIds)
        {
            out.writeVarInt(entityId);
        }
    }
}
