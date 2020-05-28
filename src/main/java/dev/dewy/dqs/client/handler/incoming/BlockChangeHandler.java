package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.protocol.game.chunk.Chunk;
import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.world.block.BlockChangeRecord;
import dev.dewy.dqs.packet.ingame.server.world.ServerBlockChangePacket;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class BlockChangeHandler implements HandlerRegistry.IncomingHandler<ServerBlockChangePacket, DQSClientSession>
{
    static void handleChange(BlockChangeRecord record)
    {
        Position pos = record.getPosition();
        if (pos.getY() < 0 || pos.getY() >= 256)
        {
            CLIENT_LOG.trace("Received out-of-bounds block update: %s", record);
            return;
        }
        Column column = CACHE.getChunkCache().get(pos.getX() >> 4, pos.getZ() >> 4);
        if (column != null)
        {
            Chunk chunk = column.getChunks()[pos.getY() >> 4];
            if (chunk == null)
            {
                chunk = column.getChunks()[pos.getY() >> 4] = new Chunk(column.hasSkylight());
            }
            chunk.getBlocks().set(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF, record.getBlock());
        }
    }

    @Override
    public boolean apply(ServerBlockChangePacket packet, DQSClientSession session)
    {
        handleChange(packet.getRecord());
        return true;
    }

    @Override
    public Class<ServerBlockChangePacket> getPacketClass()
    {
        return ServerBlockChangePacket.class;
    }
}
