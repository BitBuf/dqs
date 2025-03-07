package dev.dewy.dqs.protocol.game.world.map;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Arrays;

public class MapData
{
    private final int columns;
    private final int rows;
    private final int x;
    private final int y;
    private final byte[] data;

    public MapData(int columns, int rows, int x, int y, byte[] data)
    {
        this.columns = columns;
        this.rows = rows;
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public int getColumns()
    {
        return this.columns;
    }

    public int getRows()
    {
        return this.rows;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public byte[] getData()
    {
        return this.data;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof MapData)) return false;

        MapData that = (MapData) o;
        return this.columns == that.columns &&
                this.rows == that.rows &&
                this.x == that.x &&
                this.y == that.y &&
                Arrays.equals(this.data, that.data);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.columns, this.rows, this.x, this.y, this.data);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
