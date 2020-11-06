package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityAttachHandler implements HandlerRegistry.IncomingHandler<ServerEntityAttachPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityAttachPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            if (packet.getAttachedToId() == -1)
            {
                entity.setLeashed(false).setLeashedId(-1);
            } else
            {
                entity.setLeashed(true).setLeashedId(packet.getAttachedToId());
            }
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityAttachPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityAttachPacket> getPacketClass()
    {
        return ServerEntityAttachPacket.class;
    }
}
