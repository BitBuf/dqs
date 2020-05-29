package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.EntityEquipment;
import dev.dewy.dqs.cache.data.entity.PotionEffect;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityEffectPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityEffectHandler implements HandlerRegistry.IncomingHandler<ServerEntityEffectPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityEffectPacket packet, DQSClientSession session)
    {
        try
        {
            EntityEquipment entity = CACHE.getEntityCache().get(packet.getEntityId());
            if (entity != null)
            {
                entity.getPotionEffects().add(new PotionEffect(
                        packet.getEffect(),
                        packet.getAmplifier(),
                        packet.getDuration(),
                        packet.isAmbient(),
                        packet.getShowParticles()
                ));
            } else
            {
                CLIENT_LOG.warn("Received ServerEntityEffectPacket for invalid entity (id=%d)", packet.getEntityId());
            }
        } catch (ClassCastException e)
        {
            CLIENT_LOG.warn("Received ServerEntityEffectPacket for non-equipment entity (id=%d)", e, packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityEffectPacket> getPacketClass()
    {
        return ServerEntityEffectPacket.class;
    }
}
