package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerEntityHeadLookPacket extends MinecraftPacket
{
    private int entityId;
    private float headYaw;

    @SuppressWarnings("unused")
    private ServerEntityHeadLookPacket()
    {
    }

    public ServerEntityHeadLookPacket(int entityId, float headYaw)
    {
        this.entityId = entityId;
        this.headYaw = headYaw;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public float getHeadYaw()
    {
        return this.headYaw;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.headYaw = in.readByte() * 360 / 256f;
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeByte((byte) (this.headYaw * 256 / 360));
    }
}
