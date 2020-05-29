package dev.dewy.dqs.client.handler.incoming.entity;

import dev.dewy.dqs.cache.data.entity.Entity;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.ServerEntityMetadataPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.EntityMetadata;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class EntityMetadataHandler implements HandlerRegistry.IncomingHandler<ServerEntityMetadataPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerEntityMetadataPacket packet, DQSClientSession session)
    {
        Entity entity = CACHE.getEntityCache().get(packet.getEntityId());
        if (entity != null)
        {
            MAINLOOP:
            for (EntityMetadata metadata : packet.getMetadata())
            {
                for (int i = entity.getMetadata().size() - 1; i >= 0; i--)
                {
                    EntityMetadata old = entity.getMetadata().get(i);
                    if (old.getId() == metadata.getId())
                    {
                        entity.getMetadata().set(i, metadata);
                        continue MAINLOOP;
                    }
                }
                entity.getMetadata().add(metadata);
            }
        } else
        {
            CLIENT_LOG.warn("Received ServerEntityMetadataPacket for invalid entity (id=%d)", packet.getEntityId());
        }
        return true;
    }

    @Override
    public Class<ServerEntityMetadataPacket> getPacketClass()
    {
        return ServerEntityMetadataPacket.class;
    }
}
