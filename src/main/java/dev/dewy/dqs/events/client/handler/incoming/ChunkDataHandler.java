package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
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
