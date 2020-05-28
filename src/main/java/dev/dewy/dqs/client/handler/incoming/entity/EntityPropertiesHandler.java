package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.handler.HandlerRegistry;

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
