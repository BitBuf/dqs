package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.EquipmentSlot;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;

import java.io.IOException;

public class ServerEntityEquipmentPacket extends MinecraftPacket
{
    private int entityId;
    private EquipmentSlot slot;
    private ItemStack item;

    @SuppressWarnings("unused")
    private ServerEntityEquipmentPacket()
    {
    }

    public ServerEntityEquipmentPacket(int entityId, EquipmentSlot slot, ItemStack item)
    {
        this.entityId = entityId;
        this.slot = slot;
        this.item = item;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public EquipmentSlot getSlot()
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
        this.entityId = in.readVarInt();
        this.slot = MagicValues.key(EquipmentSlot.class, in.readVarInt());
        this.item = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeVarInt(MagicValues.value(Integer.class, this.slot));
        NetUtil.writeItem(out, this.item);
    }
}
