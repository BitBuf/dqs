package dev.dewy.dqs.taribone.world.chunk;

public class ChunkLocation
{
    private final int x, z;

    public ChunkLocation(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    @Override
    public int hashCode()
    {
        return 3;
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

        final ChunkLocation other = (ChunkLocation) obj;

        if (this.x != other.x)
        {
            return false;
        }

        return this.z == other.z;
    }

    @Override
    public String toString()
    {
        return "ChunkLocation{"
                + "x=" + x
                + ", z=" + z
                + '}';
    }
}
