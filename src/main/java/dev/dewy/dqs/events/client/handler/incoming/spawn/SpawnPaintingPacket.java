package dev.dewy.dqs.events.client.handler.incoming.spawn;

import dev.dewy.dqs.services.cache.data.entity.EntityPainting;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SpawnPaintingPacket implements HandlerRegistry.IncomingHandler<ServerSpawnPaintingPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnPaintingPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityPainting()
                .setDirection(packet.getDirection())
                .setPaintingType(packet.getPaintingType())
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getPosition().getX())
                .setY(packet.getPosition().getY())
                .setZ(packet.getPosition().getZ())
        );
        return true;
    }

    @Override
    public Class<ServerSpawnPaintingPacket> getPacketClass()
    {
        return ServerSpawnPaintingPacket.class;
    }
}
