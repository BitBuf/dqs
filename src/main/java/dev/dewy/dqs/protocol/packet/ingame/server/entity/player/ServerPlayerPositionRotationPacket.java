package dev.dewy.dqs.protocol.packet.ingame.server.entity.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.PositionElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerPlayerPositionRotationPacket extends MinecraftPacket
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private List<PositionElement> relative;
    private int teleportId;

    @SuppressWarnings("unused")
    private ServerPlayerPositionRotationPacket()
    {
    }

    public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, int teleportId, PositionElement... relative)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.teleportId = teleportId;
        this.relative = Arrays.asList(relative != null ? relative : new PositionElement[0]);
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

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public List<PositionElement> getRelativeElements()
    {
        return this.relative;
    }

    public int getTeleportId()
    {
        return this.teleportId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.relative = new ArrayList<PositionElement>();
        int flags = in.readUnsignedByte();
        for (PositionElement element : PositionElement.values())
        {
            int bit = 1 << MagicValues.value(Integer.class, element);
            if ((flags & bit) == bit)
            {
                this.relative.add(element);
            }
        }

        this.teleportId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        int flags = 0;
        for (PositionElement element : this.relative)
        {
            flags |= 1 << MagicValues.value(Integer.class, element);
        }

        out.writeByte(flags);
        out.writeVarInt(this.teleportId);
    }
}
