package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerKeepAlivePacket;

public class ClientKeepaliveHandler implements HandlerRegistry.IncomingHandler<ServerKeepAlivePacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerKeepAlivePacket packet, DQSClientSession session)
    {
        return false;
    }

    @Override
    public Class<ServerKeepAlivePacket> getPacketClass()
    {
        return ServerKeepAlivePacket.class;
    }
}
