package dev.dewy.dqs.events.client.handler.incoming.entity;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class EntityCollectItemHandler implements HandlerRegistry.IncomingHandler<ServerEntityCollectItemPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityCollectItemPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().remove(packet.getCollectedEntityId());
        return true;
    }

    @Override
    public Class<ServerEntityCollectItemPacket> getPacketClass()
    {
        return ServerEntityCollectItemPacket.class;
    }
}
