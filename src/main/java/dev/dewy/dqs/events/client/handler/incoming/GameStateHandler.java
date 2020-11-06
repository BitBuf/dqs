package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;
import dev.dewy.dqs.protocol.game.world.notify.ClientNotification;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class GameStateHandler implements HandlerRegistry.IncomingHandler<ServerNotifyClientPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerNotifyClientPacket packet, DQSClientSession session)
    {
        if (packet.getNotification() == ClientNotification.CHANGE_GAMEMODE)
        {
            CACHE.getPlayerCache().setGameMode((GameMode) packet.getValue());
        }
        return true;
    }

    @Override
    public Class<ServerNotifyClientPacket> getPacketClass()
    {
        return ServerNotifyClientPacket.class;
    }
}
