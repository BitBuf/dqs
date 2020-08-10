package dev.dewy.dqs.client.handler.incoming.spawn;

import dev.dewy.dqs.cache.data.entity.EntityMob;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

import java.util.Arrays;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SpawnMobHandler implements HandlerRegistry.IncomingHandler<ServerSpawnMobPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnMobPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityMob()
                .setMobType(packet.getType())
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch())
                .setHeadYaw(packet.getHeadYaw())
                .setVelX(packet.getMotionX())
                .setVelY(packet.getMotionY())
                .setVelZ(packet.getMotionZ())
                .setMetadata(Arrays.asList(packet.getMetadata()))
        );
        return true;
    }

    @Override
    public Class<ServerSpawnMobPacket> getPacketClass()
    {
        return ServerSpawnMobPacket.class;
    }
}
