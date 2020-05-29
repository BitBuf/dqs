package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.EntityEquipment;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityEquipmentPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityEquipmentHandler implements HandlerRegistry.IncomingHandler<ServerEntityEquipmentPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityEquipmentPacket packet, DQSClientSession session)
    {
        try
        {
            EntityEquipment entity = CACHE.getEntityCache().get(packet.getEntityId());
            if (entity != null)
            {
                entity.getEquipment().put(packet.getSlot(), packet.getItem());
            } else
            {
                CLIENT_LOG.warn("Received ServerEntityEquipmentPacket for invalid entity (id=%d)", packet.getEntityId());
            }
        } catch (ClassCastException e)
        {
            CLIENT_LOG.warn("Received ServerEntityEquipmentPacket for non-equipment entity (id=%d)", e, packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityEquipmentPacket> getPacketClass()
    {
        return ServerEntityEquipmentPacket.class;
    }
}
