package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityDestroyPacket;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class EntityDestroyHandler implements HandlerRegistry.IncomingHandler<ServerEntityDestroyPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityDestroyPacket packet, DQSClientSession session)
    {
        for (int id : packet.getEntityIds())
        {
            CACHE.getEntityCache().remove(id);
        }
        return true;
    }

    @Override
    public Class<ServerEntityDestroyPacket> getPacketClass()
    {
        return ServerEntityDestroyPacket.class;
    }
}
