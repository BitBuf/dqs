package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.services.cache.data.entity.Entity;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityTeleportHandler implements HandlerRegistry.IncomingHandler<ServerEntityTeleportPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityTeleportPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setX(packet.getX())
                    .setY(packet.getY())
                    .setZ(packet.getZ())
                    .setYaw(packet.getYaw())
                    .setPitch(packet.getPitch());
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityTeleportPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityTeleportPacket> getPacketClass()
    {
        return ServerEntityTeleportPacket.class;
    }
}
