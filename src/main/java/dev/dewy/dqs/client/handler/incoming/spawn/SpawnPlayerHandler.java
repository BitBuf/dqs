package dev.dewy.dqs.client.handler.incoming.spawn;

import dev.dewy.dqs.cache.data.entity.EntityPlayer;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

import java.util.Arrays;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SpawnPlayerHandler implements HandlerRegistry.IncomingHandler<ServerSpawnPlayerPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnPlayerPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityPlayer()
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch())
                .setMetadata(Arrays.asList(packet.getMetadata()))
        );
        return true;
    }

    @Override
    public Class<ServerSpawnPlayerPacket> getPacketClass()
    {
        return ServerSpawnPlayerPacket.class;
    }
}
