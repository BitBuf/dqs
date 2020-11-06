package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import dev.dewy.dqs.protocol.game.PlayerListEntry;

import java.util.function.Consumer;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.WEBSOCKET_SERVER;

public class TabListEntryHandler implements HandlerRegistry.IncomingHandler<ServerPlayerListEntryPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerPlayerListEntryPacket packet, DQSClientSession session)
    {
        Consumer<PlayerListEntry> consumer = entry ->
        {
            throw new IllegalStateException();
        };
        switch (packet.getAction())
        {
            case ADD_PLAYER:
                consumer = CACHE.getTabListCache().getTabList()::add;
                break;
            case REMOVE_PLAYER:
                consumer = CACHE.getTabListCache().getTabList()::remove;
                break;
            case UPDATE_LATENCY:
                consumer = entry -> WEBSOCKET_SERVER.updatePlayer(CACHE.getTabListCache().getTabList().get(entry).setPing(entry.getPing()));
                break;
            case UPDATE_DISPLAY_NAME:
                consumer = entry -> WEBSOCKET_SERVER.updatePlayer(CACHE.getTabListCache().getTabList().get(entry).setDisplayName(entry.getDisplayName()));
                break;
            case UPDATE_GAMEMODE:
                consumer = entry -> WEBSOCKET_SERVER.updatePlayer(CACHE.getTabListCache().getTabList().get(entry).setGameMode(entry.getGameMode()));
                break;
        }
        for (PlayerListEntry entry : packet.getEntries())
        {
            consumer.accept(entry);
        }
        return true;
    }

    @Override
    public Class<ServerPlayerListEntryPacket> getPacketClass()
    {
        return ServerPlayerListEntryPacket.class;
    }
}
