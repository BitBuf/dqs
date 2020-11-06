package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityPositionHandler implements HandlerRegistry.IncomingHandler<ServerEntityPositionPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityPositionPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setX(entity.getX() + packet.getMovementX())
                    .setY(entity.getY() + packet.getMovementY())
                    .setZ(entity.getZ() + packet.getMovementZ());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityPositionPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityPositionPacket> getPacketClass()
    {
        return ServerEntityPositionPacket.class;
    }
}
