package dev.dewy.dqs.protocol.packet.ingame.client.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;

import java.io.IOException;

public class ClientCreativeInventoryActionPacket extends MinecraftPacket
{
    private int slot;
    private ItemStack clicked;

    @SuppressWarnings("unused")
    private ClientCreativeInventoryActionPacket()
    {
    }

    public ClientCreativeInventoryActionPacket(int slot, ItemStack clicked)
    {
        this.slot = slot;
        this.clicked = clicked;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public ItemStack getClickedItem()
    {
        return this.clicked;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.slot = in.readShort();
        this.clicked = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.clicked);
    }
}
