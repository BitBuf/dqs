package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerEntityDestroyPacket extends MinecraftPacket
{
    private int entityIds[];

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
