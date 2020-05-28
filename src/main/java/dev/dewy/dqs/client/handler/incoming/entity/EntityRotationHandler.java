package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityRotationPacket;
import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.handler.HandlerRegistry;

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
