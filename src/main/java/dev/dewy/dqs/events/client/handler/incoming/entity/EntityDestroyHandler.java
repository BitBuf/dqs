package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;

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
