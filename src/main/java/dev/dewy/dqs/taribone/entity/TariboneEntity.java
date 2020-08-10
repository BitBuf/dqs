package dev.dewy.dqs.taribone.entity;

import dev.dewy.dqs.utils.vector.Vector3d;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an entity.
 *
 * <p>
 * http://wiki.vg/Entities#Entity</p>
 */
public class TariboneEntity
{

    protected final int id;
    protected final UUID uuid;

    protected Vector3d location;
    protected double yaw, headYaw, pitch;
    protected Vector3d velocity;
    protected boolean onGround = true;

    public TariboneEntity(int id, UUID uuid)
    {
        this.id = id;
        this.uuid = uuid;
    }

    public int getId()
    {
        return id;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public Vector3d getLocation()
    {
        return location;
    }

    public void setLocation(Vector3d location)
    {
        this.location = location;
    }

    public double getYaw()
    {
        return yaw;
    }

    public void setYaw(double yaw)
    {
        this.yaw = yaw;
    }

    public double getHeadYaw()
    {
        return headYaw;
    }

    public void setHeadYaw(double headYaw)
    {
        this.headYaw = headYaw;
    }

    public double getPitch()
    {
        return pitch;
    }

    public void setPitch(double pitch)
    {
        this.pitch = pitch;
    }

    public Vector3d getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector3d velocity)
    {
        this.velocity = velocity;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public void setOnGround(boolean onGround)
    {
        this.onGround = onGround;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.uuid);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TariboneEntity other = (TariboneEntity) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return Objects.equals(this.uuid, other.uuid);
    }
}
