package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class UnloadChunkHandler implements HandlerRegistry.IncomingHandler<ServerUnloadChunkPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerUnloadChunkPacket packet, DQSClientSession session)
    {
        CACHE.getChunkCache().remove(packet.getX(), packet.getZ());
        return true;
    }

    @Override
    public Class<ServerUnloadChunkPacket> getPacketClass()
    {
        return ServerUnloadChunkPacket.class;
    }
}
