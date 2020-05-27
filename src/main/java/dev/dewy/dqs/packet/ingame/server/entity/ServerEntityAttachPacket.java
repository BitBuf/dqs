package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerEntityAttachPacket extends MinecraftPacket
{
    private int entityId;
    private int attachedToId;

    @SuppressWarnings("unused")
    private ServerEntityAttachPacket()
    {
    }

    public ServerEntityAttachPacket(int entityId, int attachedToId)
    {
        this.entityId = entityId;
        this.attachedToId = attachedToId;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int getAttachedToId()
    {
        return this.attachedToId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readInt();
        this.attachedToId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeInt(this.entityId);
        out.writeInt(this.attachedToId);
    }
}
