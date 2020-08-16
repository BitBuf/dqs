package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class ChunkDataHandler implements HandlerRegistry.IncomingHandler<ServerChunkDataPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerChunkDataPacket packet, DQSClientSession session)
    {
        CACHE.getChunkCache().add(packet.getColumn());
        return true;
    }

    @Override
    public Class<ServerChunkDataPacket> getPacketClass()
    {
        return ServerChunkDataPacket.class;
    }
}
