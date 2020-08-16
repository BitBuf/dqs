package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;

import java.util.Arrays;
import java.util.stream.Collectors;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntitySetPassengersHandler implements HandlerRegistry.IncomingHandler<ServerEntitySetPassengersPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntitySetPassengersPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            entity.setPassengerIds(Arrays.stream(packet.getPassengerIds()).boxed().collect(Collectors.toList()));
        } else
        {
            CLIENT_LOG.warn("Received ServerEntitySetPassengersPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntitySetPassengersPacket> getPacketClass()
    {
        return ServerEntitySetPassengersPacket.class;
    }
}
