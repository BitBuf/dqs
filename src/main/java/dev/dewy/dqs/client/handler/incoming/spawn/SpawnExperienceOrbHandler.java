package dev.dewy.dqs.client.handler.incoming.spawn;

import dev.dewy.dqs.cache.data.entity.EntityExperienceOrb;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SpawnExperienceOrbHandler implements HandlerRegistry.IncomingHandler<ServerSpawnExpOrbPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnExpOrbPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityExperienceOrb()
                .setExp(packet.getExp())
                .setEntityId(packet.getEntityId())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
        );
        return true;
    }

    @Override
    public Class<ServerSpawnExpOrbPacket> getPacketClass()
    {
        return ServerSpawnExpOrbPacket.class;
    }
}
