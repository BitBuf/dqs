package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerRespawnPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class RespawnHandler implements HandlerRegistry.IncomingHandler<ServerRespawnPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerRespawnPacket packet, DQSClientSession session)
    {
        CACHE.reset(false);
        CACHE.getPlayerCache()
                .setDimension(packet.getDimension())
                .setGameMode(packet.getGameMode())
                .setWorldType(packet.getWorldType())
                .setDifficulty(packet.getDifficulty());
        return true;
    }

    @Override
    public Class<ServerRespawnPacket> getPacketClass()
    {
        return ServerRespawnPacket.class;
    }
}
