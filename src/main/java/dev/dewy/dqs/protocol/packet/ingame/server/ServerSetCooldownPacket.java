package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerSetCooldownPacket extends MinecraftPacket
{
    private int itemId;
    private int cooldownTicks;

    @SuppressWarnings("unused")
    private ServerSetCooldownPacket()
    {
    }

    public ServerSetCooldownPacket(int itemId, int cooldownTicks)
    {
        this.itemId = itemId;
        this.cooldownTicks = cooldownTicks;
    }

    public int getItemId()
    {
        return this.itemId;
    }

    public int getCooldownTicks()
    {
        return this.cooldownTicks;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.itemId = in.readVarInt();
        this.cooldownTicks = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.itemId);
        out.writeVarInt(this.cooldownTicks);
    }
}
