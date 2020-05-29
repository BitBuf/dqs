package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerBossBarPacket;

import java.util.function.Consumer;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class BossBarHandler implements HandlerRegistry.IncomingHandler<ServerBossBarPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerBossBarPacket pck, DQSClientSession session)
    {
        Consumer<ServerBossBarPacket> consumer = packet ->
        {
            throw new IllegalStateException();
        };
        switch (pck.getAction())
        {
            case ADD:
                consumer = CACHE.getBossBarCache()::add;
                break;
            case REMOVE:
                consumer = CACHE.getBossBarCache()::remove;
                break;
            case UPDATE_HEALTH:
                consumer = packet -> CACHE.getBossBarCache().get(packet).setHealth(packet.getHealth());
                break;
            case UPDATE_TITLE:
                consumer = packet -> CACHE.getBossBarCache().get(packet).setTitle(packet.getTitle());
                break;
            case UPDATE_STYLE:
                consumer = packet -> CACHE.getBossBarCache().get(packet).setColor(packet.getColor()).setDivision(packet.getDivision());
                break;
            case UPDATE_FLAGS:
                consumer = packet -> CACHE.getBossBarCache().get(packet).setDarkenSky(packet.getDarkenSky()).setDragonBar(packet.isDragonBar());
                break;
        }
        consumer.accept(pck);
        return true;
    }

    @Override
    public Class<ServerBossBarPacket> getPacketClass()
    {
        return ServerBossBarPacket.class;
    }
}
