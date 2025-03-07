package dev.dewy.dqs.protocol.packet.ingame.client.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ClientEnchantItemPacket extends MinecraftPacket
{
    private int windowId;
    private int enchantment;

    @SuppressWarnings("unused")
    private ClientEnchantItemPacket()
    {
    }

    public ClientEnchantItemPacket(int windowId, int enchantment)
    {
        this.windowId = windowId;
        this.enchantment = enchantment;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getEnchantment()
    {
        return this.enchantment;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.windowId = in.readByte();
        this.enchantment = in.readByte();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
        out.writeByte(this.enchantment);
    }
}
