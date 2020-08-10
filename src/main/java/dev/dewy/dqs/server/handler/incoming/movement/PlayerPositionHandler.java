package dev.dewy.dqs.server.handler.incoming.movement;

import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.client.player.ClientPlayerPositionPacket;
import dev.dewy.dqs.server.DQSServerConnection;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class PlayerPositionHandler implements HandlerRegistry.IncomingHandler<ClientPlayerPositionPacket, DQSServerConnection>
{
    @Override
    public boolean apply(ClientPlayerPositionPacket packet, DQSServerConnection session)
    {
        CACHE.getPlayerCache()
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ());
        return true;
    }

    @Override
    public Class<ClientPlayerPositionPacket> getPacketClass()
    {
        return ClientPlayerPositionPacket.class;
    }
}
