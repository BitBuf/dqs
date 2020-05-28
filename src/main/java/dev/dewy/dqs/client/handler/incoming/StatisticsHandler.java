package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.packet.ingame.server.ServerStatisticsPacket;
import net.daporkchop.lib.unsafe.PUnsafe;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;

import java.util.HashMap;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class StatisticsHandler implements HandlerRegistry.IncomingHandler<ServerStatisticsPacket, DQSClientSession>
{
    protected static final long PACKET_STATISTICS_OFFSET = PUnsafe.pork_getOffset(ServerStatisticsPacket.class, "statistics");

    @Override
    public boolean apply(ServerStatisticsPacket packet, DQSClientSession session)
    {
        CACHE.getStatsCache().getStatistics().putAll(packet.getStatistics()); //cache all locally
        PUnsafe.putObject(packet, PACKET_STATISTICS_OFFSET, new HashMap<>(CACHE.getStatsCache().getStatistics())); //replace statistics packet with copy of local
        return true;
    }

    @Override
    public Class<ServerStatisticsPacket> getPacketClass()
    {
        return ServerStatisticsPacket.class;
    }
}
