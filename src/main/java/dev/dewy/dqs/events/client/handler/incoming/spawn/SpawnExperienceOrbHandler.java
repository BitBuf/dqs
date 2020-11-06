package dev.dewy.dqs.events.client.handler.incoming.spawn;

import dev.dewy.dqs.services.cache.data.entity.EntityExperienceOrb;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;

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
