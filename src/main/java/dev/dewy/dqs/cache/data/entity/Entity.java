package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import dev.dewy.dqs.protocol.game.entity.attribute.Attribute;
import dev.dewy.dqs.protocol.game.entity.metadata.EntityMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Entity
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected float headYaw;
    protected int entityId;
    protected UUID uuid;
    protected double velX;
    protected double velY;
    protected double velZ;
    protected int leashedId;
    protected boolean isLeashed;
    protected List<Attribute> properties = new ArrayList<>();
    protected List<EntityMetadata> metadata = new ArrayList<>();
    protected List<Integer> passengerIds = new ArrayList<>(); //TODO: primitive list

    public void addPackets(Consumer<Packet> consumer)
    {
        if (!this.properties.isEmpty())
        {
            consumer.accept(new ServerEntityPropertiesPacket(this.entityId, this.properties));
        }
        if (!this.passengerIds.isEmpty())
        {
            consumer.accept(new ServerEntitySetPassengersPacket(this.entityId, this.getPassengerIdsAsArray()));
        }
    }

    public int[] getPassengerIdsAsArray()
    {
        int[] arr = new int[this.passengerIds.size()];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = this.passengerIds.get(i);
        }
        return arr;
    }

    public double getX()
    {
        return this.x;
    }

    public Entity setX(double x)
    {
        this.x = x;
        return this;
    }

    public double getY()
    {
        return this.y;
    }

    public Entity setY(double y)
    {
        this.y = y;
        return this;
    }

    public double getZ()
    {
        return this.z;
    }

    public Entity setZ(double z)
    {
        this.z = z;
        return this;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public Entity setYaw(float yaw)
    {
        this.yaw = yaw;
        return this;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public Entity setPitch(float pitch)
    {
        this.pitch = pitch;
        return this;
    }

    public float getHeadYaw()
    {
        return this.headYaw;
    }

    public Entity setHeadYaw(float headYaw)
    {
        this.headYaw = headYaw;
        return this;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Entity setEntityId(int entityId)
    {
        this.entityId = entityId;
        return this;
    }

    public UUID getUuid()
    {
        return this.uuid;
    }

    public Entity setUuid(UUID uuid)
    {
        this.uuid = uuid;
        return this;
    }

    public double getVelX()
    {
        return this.velX;
    }

    public Entity setVelX(double velX)
    {
        this.velX = velX;
        return this;
    }

    public double getVelY()
    {
        return this.velY;
    }

    public Entity setVelY(double velY)
    {
        this.velY = velY;
        return this;
    }

    public double getVelZ()
    {
        return this.velZ;
    }

    public Entity setVelZ(double velZ)
    {
        this.velZ = velZ;
        return this;
    }

    public int getLeashedId()
    {
        return this.leashedId;
    }

    public Entity setLeashedId(int leashedId)
    {
        this.leashedId = leashedId;
        return this;
    }

    public boolean isLeashed()
    {
        return this.isLeashed;
    }

    public Entity setLeashed(boolean isLeashed)
    {
        this.isLeashed = isLeashed;
        return this;
    }

    public List<Attribute> getProperties()
    {
        return this.properties;
    }

    public Entity setProperties(List<Attribute> properties)
    {
        this.properties = properties;
        return this;
    }

    public List<EntityMetadata> getMetadata()
    {
        return this.metadata;
    }

    public Entity setMetadata(List<EntityMetadata> metadata)
    {
        this.metadata = metadata;
        return this;
    }

    public List<Integer> getPassengerIds()
    {
        return this.passengerIds;
    }

    public Entity setPassengerIds(List<Integer> passengerIds)
    {
        this.passengerIds = passengerIds;
        return this;
    }
}
