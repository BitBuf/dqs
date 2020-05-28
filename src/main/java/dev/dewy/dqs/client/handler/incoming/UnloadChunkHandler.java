package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.packet.ingame.server.world.ServerUnloadChunkPacket;
import dev.dewy.dqs.handler.HandlerRegistry;

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
