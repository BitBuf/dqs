package dev.dewy.dqs.packet.ingame.client.player;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class ClientPlayerChangeHeldItemPacket extends MinecraftPacket
{
    private int slot;

    @SuppressWarnings("unused")
    private ClientPlayerChangeHeldItemPacket()
    {
    }

    public ClientPlayerChangeHeldItemPacket(int slot)
    {
        this.slot = slot;
    }

    public int getSlot()
    {
        return this.slot;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.slot = in.readShort();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeShort(this.slot);
    }
}
