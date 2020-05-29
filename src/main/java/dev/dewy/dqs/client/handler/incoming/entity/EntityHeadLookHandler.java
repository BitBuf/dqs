package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityHeadLookPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityHeadLookHandler implements HandlerRegistry.IncomingHandler<ServerEntityHeadLookPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityHeadLookPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setHeadYaw(packet.getHeadYaw());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityHeadLookPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityHeadLookPacket> getPacketClass()
    {
        return ServerEntityHeadLookPacket.class;
    }
}
