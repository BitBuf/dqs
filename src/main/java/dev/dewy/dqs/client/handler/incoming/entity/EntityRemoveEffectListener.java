package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.EntityEquipment;
import dev.dewy.dqs.cache.data.entity.PotionEffect;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;

import java.util.Iterator;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityRemoveEffectListener implements HandlerRegistry.IncomingHandler<ServerEntityRemoveEffectPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityRemoveEffectPacket packet, DQSClientSession session)
    {
        try
        {
            EntityEquipment entity = CACHE.getEntityCache().get(packet.getEntityId());
            if (entity != null)
            {
                for (Iterator<PotionEffect> iterator = entity.getPotionEffects().iterator(); iterator.hasNext(); )
                {
                    if (iterator.next().effect == packet.getEffect())
                    {
                        iterator.remove();
                        break;
                    }
                }
            } else
            {
                CLIENT_LOG.warn("Received ServerEntityRemoveEffectPacket for invalid entity (id=%d)", packet.getEntityId());
            }
        } catch (ClassCastException e)
        {
            CLIENT_LOG.warn("Received ServerEntityRemoveEffectPacket for non-equipment entity (id=%d)", e, packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityRemoveEffectPacket> getPacketClass()
    {
        return ServerEntityRemoveEffectPacket.class;
    }
}
