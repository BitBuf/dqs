package dev.dewy.dqs.protocol.game.entity.metadata;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class EntityMetadata
{
    private final int id;
    private final MetadataType type;
    private final Object value;

    public EntityMetadata(int id, MetadataType type, Object value)
    {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public int getId()
    {
        return this.id;
    }

    public MetadataType getType()
    {
        return this.type;
    }

    public Object getValue()
    {
        return this.value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof EntityMetadata)) return false;

        EntityMetadata that = (EntityMetadata) o;
        return this.id == that.id &&
                this.type == that.type &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.id, this.type, this.value);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
