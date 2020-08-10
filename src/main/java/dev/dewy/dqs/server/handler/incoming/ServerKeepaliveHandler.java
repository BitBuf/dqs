package dev.dewy.dqs.server.handler.incoming;

import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.client.ClientKeepAlivePacket;
import dev.dewy.dqs.server.DQSServerConnection;

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
