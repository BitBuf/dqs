package dev.dewy.dqs.events.client.handler.incoming.spawn;

import dev.dewy.dqs.services.cache.data.entity.EntityMob;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

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
