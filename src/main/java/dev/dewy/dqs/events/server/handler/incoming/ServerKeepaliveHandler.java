package dev.dewy.dqs.events.server.handler.incoming;

import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.client.ClientKeepAlivePacket;
import dev.dewy.dqs.events.server.DQSServerConnection;

public class ServerKeepaliveHandler implements HandlerRegistry.IncomingHandler<ClientKeepAlivePacket, DQSServerConnection>
{
    @Override
    public boolean apply(ClientKeepAlivePacket packet, DQSServerConnection session)
    {
        return false;
    }

    @Override
    public Class<ClientKeepAlivePacket> getPacketClass()
    {
        return ClientKeepAlivePacket.class;
    }
}
