package dev.dewy.dqs.taribone.world.chunk;

import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.taribone.world.World;

/**
 * Represents a vertical column of 16x16x16 chunks.
 */
public class Chunk
{
    private final World world;
    private final ChunkLocation location;
    private final Column handle;

    public Chunk(World world, Column column)
    {
        this.world = world;
        this.handle = column;
        this.location = new ChunkLocation(column.getX(), column.getZ());
    }

    public Column getHandle()
    {
        return handle;
    }

    public World getWorld()
    {
        return world;
    }

    public ChunkLocation getLocation()
    {
        return this.location;
    }

    @Override
    public int hashCode()
    {
        return 7;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Chunk chunk = (Chunk) o;

        if (!world.equals(chunk.world))
        {
            return false;
        }
        if (!location.equals(chunk.location))
        {
            return false;
        }
        return handle.equals(chunk.handle);
    }
}
