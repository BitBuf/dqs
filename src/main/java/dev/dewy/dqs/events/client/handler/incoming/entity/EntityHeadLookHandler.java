package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;

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
