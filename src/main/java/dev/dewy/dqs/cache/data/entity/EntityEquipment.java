package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityEffectPacket;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import dev.dewy.dqs.protocol.game.entity.EquipmentSlot;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class EntityEquipment extends Entity
{
    protected ArrayList<PotionEffect> potionEffects = new ArrayList<>();
    protected Map<EquipmentSlot, ItemStack> equipment = new EnumMap<>(EquipmentSlot.class);
    protected float health;

    {
        for (EquipmentSlot slot : EquipmentSlot.values())
        {
            this.equipment.put(slot, null);
        }
    }

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        this.potionEffects.forEach(effect -> consumer.accept(new ServerEntityEffectPacket(
                this.entityId,
                effect.getEffect(),
                effect.getAmplifier(),
                effect.getDuration(),
                effect.isAmbient(),
                effect.isShowParticles()
        )));
        this.equipment.forEach((slot, stack) -> consumer.accept(new ServerEntityEquipmentPacket(this.entityId, slot, stack)));
        super.addPackets(consumer);
    }

    public ArrayList<PotionEffect> getPotionEffects()
    {
        return this.potionEffects;
    }

    public EntityEquipment setPotionEffects(ArrayList<PotionEffect> potionEffects)
    {
        this.potionEffects = potionEffects;
        return this;
    }

    public Map<EquipmentSlot, ItemStack> getEquipment()
    {
        return this.equipment;
    }

    public EntityEquipment setEquipment(Map<EquipmentSlot, ItemStack> equipment)
    {
        this.equipment = equipment;
        return this;
    }

    public float getHealth()
    {
        return this.health;
    }

    public EntityEquipment setHealth(float health)
    {
        this.health = health;
        return this;
    }
}
