package dev.dewy.dqs.utils.exceptions.taribone;

import dev.dewy.dqs.taribone.world.chunk.ChunkLocation;

public class ChunkNotLoadedException extends Exception
{
    public ChunkNotLoadedException(ChunkLocation location)
    {
        super("Chunk not loaded: (" + location.getX() + ", " + location.getZ() + ")");
    }
}
