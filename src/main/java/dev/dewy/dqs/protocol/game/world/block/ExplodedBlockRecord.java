package dev.dewy.dqs.protocol.game.world.block;

import dev.dewy.dqs.utils.ObjectUtil;

public class ExplodedBlockRecord
{
    private final int x;
    private final int y;
    private final int z;

    public ExplodedBlockRecord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ExplodedBlockRecord)) return false;

        ExplodedBlockRecord that = (ExplodedBlockRecord) o;
        return this.x == that.x &&
                this.y == that.y &&
                this.z == that.z;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.x, this.y, this.z);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
