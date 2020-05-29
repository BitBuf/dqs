package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import dev.dewy.dqs.protocol.game.entity.type.object.ObjectData;
import dev.dewy.dqs.protocol.game.entity.type.object.ObjectType;

import java.util.function.Consumer;

public class EntityObject extends Entity
{
    protected ObjectType objectType;
    protected ObjectData data;

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerSpawnObjectPacket(
                this.entityId,
                this.uuid,
                this.objectType,
                this.data,
                this.x,
                this.y,
                this.z,
                this.yaw,
                this.pitch,
                this.velX,
                this.velY,
                this.velZ
        ));
        super.addPackets(consumer);
    }

    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    public EntityObject setObjectType(ObjectType objectType)
    {
        this.objectType = objectType;
        return this;
    }

    public ObjectData getData()
    {
        return this.data;
    }

    public EntityObject setData(ObjectData data)
    {
        this.data = data;
        return this;
    }
}
