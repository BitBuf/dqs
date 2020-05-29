package dev.dewy.dqs.client.handler.incoming.spawn;

import dev.dewy.dqs.cache.data.entity.EntityObject;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SpawnObjectHandler implements HandlerRegistry.IncomingHandler<ServerSpawnObjectPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnObjectPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityObject()
                .setObjectType(packet.getType())
                .setData(packet.getData())
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch())
                .setVelX(packet.getMotionX())
                .setVelY(packet.getMotionY())
                .setVelZ(packet.getMotionZ())
        );
        return true;
    }

    @Override
    public Class<ServerSpawnObjectPacket> getPacketClass()
    {
        return ServerSpawnObjectPacket.class;
    }
}
