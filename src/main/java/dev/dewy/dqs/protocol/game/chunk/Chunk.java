package dev.dewy.dqs.protocol.game.chunk;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class Chunk
{
    private final BlockStorage blocks;
    private final NibbleArray3d blocklight;
    private final NibbleArray3d skylight;

    public Chunk(boolean skylight)
    {
        this(new BlockStorage(), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public Chunk(BlockStorage blocks, NibbleArray3d blocklight, NibbleArray3d skylight)
    {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    public BlockStorage getBlocks()
    {
        return this.blocks;
    }

    public NibbleArray3d getBlockLight()
    {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight()
    {
        return this.skylight;
    }

    public boolean isEmpty()
    {
        return this.blocks.isEmpty();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Chunk)) return false;

        Chunk that = (Chunk) o;
        return Objects.equals(this.blocks, that.blocks) &&
                Objects.equals(this.blocklight, that.blocklight) &&
                Objects.equals(this.skylight, that.skylight);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.blocks, this.blocklight, this.skylight);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
