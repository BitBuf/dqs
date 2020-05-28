package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.protocol.game.entity.player.Hand;
import dev.dewy.dqs.protocol.game.setting.ChatVisibility;
import dev.dewy.dqs.protocol.game.setting.SkinPart;
import dev.dewy.dqs.packet.ingame.client.ClientSettingsPacket;
import dev.dewy.dqs.packet.ingame.server.ServerJoinGamePacket;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class JoinGameHandler implements HandlerRegistry.IncomingHandler<ServerJoinGamePacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerJoinGamePacket packet, DQSClientSession session)
    {
        CACHE.getPlayerCache()
                .setEntityId(packet.getEntityId())
                .setDimension(packet.getDimension())
                .setWorldType(packet.getWorldType())
                .setGameMode(packet.getGameMode())
                .setDifficulty(packet.getDifficulty())
                .setHardcore(packet.getHardcore())
                .setMaxPlayers(packet.getMaxPlayers())
                .setReducedDebugInfo(packet.getReducedDebugInfo());

        session.send(new ClientSettingsPacket(
                "en_US",
                8,
                ChatVisibility.FULL,
                true,
                SkinPart.values(),
                Hand.MAIN_HAND
        ));
        return false;
    }

    @Override
    public Class<ServerJoinGamePacket> getPacketClass()
    {
        return ServerJoinGamePacket.class;
    }
}
