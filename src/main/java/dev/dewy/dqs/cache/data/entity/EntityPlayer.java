package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.EntityMetadata;

import java.util.function.Consumer;

public class EntityPlayer extends EntityEquipment
{
    protected boolean selfPlayer;

    protected int food = 20;
    protected float saturation = 20;

    public EntityPlayer(boolean selfPlayer)
    {
        this.selfPlayer = selfPlayer;
    }

    public EntityPlayer()
    {
    }

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        if (this.selfPlayer)
        {
            consumer.accept(new ServerPlayerHealthPacket(
                    this.health,
                    this.food,
                    this.saturation
            ));
        } else
        {
            consumer.accept(new ServerSpawnPlayerPacket(
                    this.entityId,
                    this.uuid,
                    this.x,
                    this.y,
                    this.z,
                    this.yaw,
                    this.pitch,
                    this.metadata.toArray(new EntityMetadata[0])
            ));
        }
        super.addPackets(consumer);
    }

    public boolean isSelfPlayer()
    {
        return this.selfPlayer;
    }

    public EntityPlayer setSelfPlayer(boolean selfPlayer)
    {
        this.selfPlayer = selfPlayer;
        return this;
    }

    public int getFood()
    {
        return this.food;
    }

    public EntityPlayer setFood(int food)
    {
        this.food = food;
        return this;
    }

    public float getSaturation()
    {
        return this.saturation;
    }

    public EntityPlayer setSaturation(float saturation)
    {
        this.saturation = saturation;
        return this;
    }
}
