package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerKeepAlivePacket;

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
