package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityRotationHandler implements HandlerRegistry.IncomingHandler<ServerEntityRotationPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityRotationPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setYaw(packet.getYaw())
                    .setPitch(packet.getPitch());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityRotationPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityRotationPacket> getPacketClass()
    {
        return ServerEntityRotationPacket.class;
    }
}
