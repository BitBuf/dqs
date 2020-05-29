package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerAdvancementsPacket;

import java.util.HashMap;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class AdvancementsHandler implements HandlerRegistry.IncomingHandler<ServerAdvancementsPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerAdvancementsPacket packet, DQSClientSession session)
    {
        if (packet.doesReset())
        {
            CACHE.getStatsCache().getAdvancements().clear();
            CACHE.getStatsCache().getProgress().clear();
        }
        CACHE.getStatsCache().getAdvancements().addAll(packet.getAdvancements());
        CACHE.getStatsCache().getAdvancements().removeIf(advancement -> packet.getRemovedAdvancements().contains(advancement.getId()));
        packet.getProgress().forEach((id, criterions) -> CACHE.getStatsCache().getProgress().computeIfAbsent(id, s -> new HashMap<>()).putAll(criterions));
        return true;
    }

    @Override
    public Class<ServerAdvancementsPacket> getPacketClass()
    {
        return ServerAdvancementsPacket.class;
    }
}
