package dev.dewy.dqs.cache.data.chunk;

import dev.dewy.dqs.protocol.game.chunk.Chunk;
import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.packet.ingame.server.world.ServerChunkDataPacket;
import dev.dewy.dqs.packet.Packet;
import net.daporkchop.lib.math.vector.i.Vec2i;
import dev.dewy.dqs.cache.CachedData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static dev.dewy.dqs.utils.Constants.CACHE_LOG;

public class ChunkCache implements CachedData, BiFunction<Column, Column, Column>
{
    protected final Map<Vec2i, Column> cache = new ConcurrentHashMap<>();

    public void add(Column column)
    {
        this.cache.merge(new Vec2i(column.getX(), column.getZ()), column, this);
        CACHE_LOG.debug("Cached chunk (%d, %d)", column.getX(), column.getZ());
    }

    /**
     * @deprecated do not call this directly!
     */
    @Override
    @Deprecated
    public Column apply(Column existing, Column add)
    {
        CACHE_LOG.trace("Chunk (%d, %d) is already cached, merging with existing", add.getX(), add.getZ());
        Chunk[] chunks = existing.getChunks().clone();
        for (int chunkY = 0; chunkY < 16; chunkY++)
        {
            Chunk addChunk = add.getChunks()[chunkY];
            if (addChunk == null)
            {
                continue;
            } else if (add.hasSkylight())
            {
                chunks[chunkY] = addChunk;
            } else
            {
                chunks[chunkY] = new Chunk(addChunk.getBlocks(), addChunk.getBlockLight(), chunks[chunkY] == null ? null : chunks[chunkY].getSkyLight());
            }
        }

        return new Column(
                add.getX(), add.getZ(),
                chunks,
                add.hasBiomeData() ? add.getBiomeData() : existing.getBiomeData(),
                add.getTileEntities());
    }

    public Column get(int x, int z)
    {
        return this.cache.get(new Vec2i(x, z));
    }

    public void remove(int x, int z)
    {
        CACHE_LOG.debug("Server telling us to uncache chunk (%d, %d)", x, z);
        if (this.cache.remove(new Vec2i(x, z)) == null)
        {
            CACHE_LOG.warn("Could not remove column (%d, %d)! this is probably a server issue", x, z);
        }
    }

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        this.cache.values().stream()
                .map(ServerChunkDataPacket::new)
                .forEach(consumer);
    }

    @Override
    public void reset(boolean full)
    {
        this.cache.clear();
    }

    @Override
    public String getSendingMessage()
    {
        return String.format("Sending %d chunks", this.cache.size());
    }
}
