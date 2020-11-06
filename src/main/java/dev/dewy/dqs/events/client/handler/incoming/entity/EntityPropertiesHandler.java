package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityPropertiesHandler implements HandlerRegistry.IncomingHandler<ServerEntityPropertiesPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityPropertiesPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setProperties(packet.getAttributes());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityPropertiesPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityPropertiesPacket> getPacketClass()
    {
        return ServerEntityPropertiesPacket.class;
    }
}
