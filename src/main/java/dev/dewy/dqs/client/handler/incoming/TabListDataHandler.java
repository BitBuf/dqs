package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.packet.ingame.server.ServerPlayerListDataPacket;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.WEBSOCKET_SERVER;

public class TabListDataHandler implements HandlerRegistry.IncomingHandler<ServerPlayerListDataPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerPlayerListDataPacket packet, DQSClientSession session)
    {
        CACHE.getTabListCache().getTabList()
                .setHeader(packet.getHeader())
                .setFooter(packet.getFooter());
        WEBSOCKET_SERVER.firePlayerListUpdate();
        return true;
    }

    @Override
    public Class<ServerPlayerListDataPacket> getPacketClass()
    {
        return ServerPlayerListDataPacket.class;
    }
}
