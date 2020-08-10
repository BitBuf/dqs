package dev.dewy.dqs.packet.ingame.server.window;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;

import java.io.IOException;

public class ServerSetSlotPacket extends MinecraftPacket
{
    private int windowId;
    private int slot;
    private ItemStack item;

    @SuppressWarnings("unused")
    private ServerSetSlotPacket()
    {
    }

    public ServerSetSlotPacket(int windowId, int slot, ItemStack item)
    {
        this.windowId = windowId;
        this.slot = slot;
        this.item = item;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.windowId = in.readUnsignedByte();
        this.slot = in.readShort();
        this.item = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.item);
    }
}
