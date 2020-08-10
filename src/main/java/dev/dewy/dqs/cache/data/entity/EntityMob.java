package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.EntityMetadata;
import dev.dewy.dqs.protocol.game.entity.type.MobType;

import java.util.function.Consumer;

public class EntityMob extends EntityEquipment
{
    protected MobType mobType;

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerSpawnMobPacket(
                this.entityId,
                this.uuid,
                this.mobType,
                this.x,
                this.y,
                this.z,
                this.yaw,
                this.pitch,
                this.headYaw,
                this.velX,
                this.velY,
                this.velZ,
                this.metadata.toArray(new EntityMetadata[0])
        ));
        super.addPackets(consumer);
    }

    public MobType getMobType()
    {
        return this.mobType;
    }

    public EntityMob setMobType(MobType mobType)
    {
        this.mobType = mobType;
        return this;
    }
}
