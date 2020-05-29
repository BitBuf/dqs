package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.cache.data.PlayerCache;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import dev.dewy.dqs.protocol.game.entity.player.PositionElement;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class PlayerPosRotHandler implements HandlerRegistry.IncomingHandler<ServerPlayerPositionRotationPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerPlayerPositionRotationPacket packet, DQSClientSession session)
    {
        PlayerCache cache = CACHE.getPlayerCache();
        cache
                .setX((packet.getRelativeElements().contains(PositionElement.X) ? cache.getX() : 0.0d) + packet.getX())
                .setY((packet.getRelativeElements().contains(PositionElement.Y) ? cache.getY() : 0.0d) + packet.getY())
                .setZ((packet.getRelativeElements().contains(PositionElement.Z) ? cache.getZ() : 0.0d) + packet.getZ())
                .setYaw((packet.getRelativeElements().contains(PositionElement.YAW) ? cache.getYaw() : 0.0f) + packet.getYaw())
                .setPitch((packet.getRelativeElements().contains(PositionElement.PITCH) ? cache.getPitch() : 0.0f) + packet.getPitch());
        return true;
    }

    @Override
    public Class<ServerPlayerPositionRotationPacket> getPacketClass()
    {
        return ServerPlayerPositionRotationPacket.class;
    }
}
