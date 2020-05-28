package dev.dewy.dqs.server.handler.incoming.movement;

import dev.dewy.dqs.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class PlayerPositionRotationHandler implements HandlerRegistry.IncomingHandler<ClientPlayerPositionRotationPacket, DQSServerConnection>
{
    @Override
    public boolean apply(ClientPlayerPositionRotationPacket packet, DQSServerConnection session)
    {
        CACHE.getPlayerCache()
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw((float) packet.getYaw())
                .setPitch((float) packet.getPitch());
        return true;
    }

    @Override
    public Class<ClientPlayerPositionRotationPacket> getPacketClass()
    {
        return ClientPlayerPositionRotationPacket.class;
    }
}
