package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.EntityStatus;

import java.io.IOException;

public class ServerEntityStatusPacket extends MinecraftPacket
{
    protected int entityId;
    protected EntityStatus status;

    @SuppressWarnings("unused")
    private ServerEntityStatusPacket()
    {
    }

    public ServerEntityStatusPacket(int entityId, EntityStatus status)
    {
        this.entityId = entityId;
        this.status = status;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public EntityStatus getStatus()
    {
        return this.status;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readInt();
        this.status = MagicValues.key(EntityStatus.class, in.readByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.status));
    }
}
