package dev.dewy.dqs.server.handler.incoming.movement;

import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import dev.dewy.dqs.server.DQSServerConnection;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class PlayerRotationHandler implements HandlerRegistry.IncomingHandler<ClientPlayerRotationPacket, DQSServerConnection>
{
    @Override
    public boolean apply(ClientPlayerRotationPacket packet, DQSServerConnection session)
    {
        CACHE.getPlayerCache()
                .setYaw((float) packet.getYaw())
                .setPitch((float) packet.getPitch());
        return true;
    }

    @Override
    public Class<ClientPlayerRotationPacket> getPacketClass()
    {
        return ClientPlayerRotationPacket.class;
    }
}
