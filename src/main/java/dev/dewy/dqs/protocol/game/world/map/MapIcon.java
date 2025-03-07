package dev.dewy.dqs.protocol.game.world.map;

import dev.dewy.dqs.utils.ObjectUtil;

public class MapIcon
{
    private final int centerX;
    private final int centerZ;
    private final MapIconType iconType;
    private final int iconRotation;

    public MapIcon(int centerX, int centerZ, MapIconType iconType, int iconRotation)
    {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.iconType = iconType;
        this.iconRotation = iconRotation;
    }

    public int getCenterX()
    {
        return this.centerX;
    }

    public int getCenterZ()
    {
        return this.centerZ;
    }

    public MapIconType getIconType()
    {
        return this.iconType;
    }

    public int getIconRotation()
    {
        return this.iconRotation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof MapIcon)) return false;

        MapIcon that = (MapIcon) o;
        return this.centerX == that.centerX &&
                this.centerZ == that.centerZ &&
                this.iconType == that.iconType &&
                this.iconRotation == that.iconRotation;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.centerX, this.centerZ, this.iconType, this.iconRotation);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
