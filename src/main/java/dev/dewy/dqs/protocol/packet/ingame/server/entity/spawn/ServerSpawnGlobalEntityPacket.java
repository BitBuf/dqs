package dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.type.GlobalEntityType;

import java.io.IOException;

public class ServerSpawnGlobalEntityPacket extends MinecraftPacket
{
    private int entityId;
    private GlobalEntityType type;
    private double x;
    private double y;
    private double z;

    @SuppressWarnings("unused")
    private ServerSpawnGlobalEntityPacket()
    {
    }

    public ServerSpawnGlobalEntityPacket(int entityId, GlobalEntityType type, double x, double y, double z)
    {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public GlobalEntityType getType()
    {
        return this.type;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.type = MagicValues.key(GlobalEntityType.class, in.readByte());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
    }
}
