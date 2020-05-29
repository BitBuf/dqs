package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityPositionRotationPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityPositionRotationHandler implements HandlerRegistry.IncomingHandler<ServerEntityPositionRotationPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityPositionRotationPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setYaw(packet.getYaw())
                    .setPitch(packet.getPitch())
                    .setX(entity.getX() + packet.getMovementX())
                    .setY(entity.getY() + packet.getMovementY())
                    .setZ(entity.getZ() + packet.getMovementZ());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityPositionRotationPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityPositionRotationPacket> getPacketClass()
    {
        return ServerEntityPositionRotationPacket.class;
    }
}
