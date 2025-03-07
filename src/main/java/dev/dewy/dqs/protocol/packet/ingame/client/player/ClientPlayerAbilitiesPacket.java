package dev.dewy.dqs.protocol.packet.ingame.client.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ClientPlayerAbilitiesPacket extends MinecraftPacket
{
    private boolean invincible;
    private boolean canFly;
    private boolean flying;
    private boolean creative;
    private float flySpeed;
    private float walkSpeed;

    @SuppressWarnings("unused")
    private ClientPlayerAbilitiesPacket()
    {
    }

    public ClientPlayerAbilitiesPacket(boolean invincible, boolean canFly, boolean flying, boolean creative, float flySpeed, float walkSpeed)
    {
        this.invincible = invincible;
        this.canFly = canFly;
        this.flying = flying;
        this.creative = creative;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public boolean getInvincible()
    {
        return this.invincible;
    }

    public boolean getCanFly()
    {
        return this.canFly;
    }

    public boolean getFlying()
    {
        return this.flying;
    }

    public boolean getCreative()
    {
        return this.creative;
    }

    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        byte flags = in.readByte();
        this.invincible = (flags & 1) > 0;
        this.canFly = (flags & 2) > 0;
        this.flying = (flags & 4) > 0;
        this.creative = (flags & 8) > 0;
        this.flySpeed = in.readFloat();
        this.walkSpeed = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        byte flags = 0;
        if (this.invincible)
        {
            flags = (byte) (flags | 1);
        }

        if (this.canFly)
        {
            flags = (byte) (flags | 2);
        }

        if (this.flying)
        {
            flags = (byte) (flags | 4);
        }

        if (this.creative)
        {
            flags = (byte) (flags | 8);
        }

        out.writeByte(flags);
        out.writeFloat(this.flySpeed);
        out.writeFloat(this.walkSpeed);
    }
}
