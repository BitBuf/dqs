package dev.dewy.dqs.protocol.game.entity.type.object;

import dev.dewy.dqs.utils.ObjectUtil;

public class FallingBlockData implements ObjectData
{
    private final int id;
    private final int metadata;

    public FallingBlockData(int id, int metadata)
    {
        this.id = id;
        this.metadata = metadata;
    }

    public int getId()
    {
        return this.id;
    }

    public int getMetadata()
    {
        return this.metadata;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FallingBlockData)) return false;

        FallingBlockData that = (FallingBlockData) o;
        return this.id == that.id &&
                this.metadata == that.metadata;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.id, this.metadata);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
