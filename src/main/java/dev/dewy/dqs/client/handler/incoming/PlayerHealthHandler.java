package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.client.ClientRequestPacket;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import dev.dewy.dqs.protocol.game.ClientRequest;
import dev.dewy.dqs.utils.Constants;
import net.daporkchop.lib.common.util.PorkUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.*;

public class PlayerHealthHandler implements HandlerRegistry.IncomingHandler<ServerPlayerHealthPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerPlayerHealthPacket packet, DQSClientSession session)
    {
        CACHE.getPlayerCache().getThePlayer()
                .setFood(packet.getFood())
                .setSaturation(packet.getSaturation())
                .setHealth(packet.getHealth());
        CACHE_LOG.debug("Player food: %d", packet.getFood())
                .debug("Player saturation: %f", packet.getSaturation())
                .debug("Player health: %f", packet.getHealth());
        if (packet.getHealth() <= 0 && CONFIG.modules.autoRespawn.enabled)
        {
            new Thread(() ->
            {
                PorkUtil.sleep(CONFIG.modules.autoRespawn.delaySeconds);
                if (CACHE.getPlayerCache().getThePlayer().getHealth() <= 0)
                {
                    CACHE.getChunkCache().reset(true); //i don't think this is needed, but it can't hurt
                    DQS.getInstance().getClient().getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
                }
            }).start();
        }

        if (packet.getHealth() <= CONFIG.modules.autoDisconnect.lowHpThreshold)
        {
            DQS.getInstance().getClient().getSession().disconnect("user disconnect", false);

            Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Auto Disconnect")
                            .setDescription("You were automatically disconnected and **saved** due to your health reaching " + (Math.round(packet.getHealth() * 2.0F) / 2.0F) + " hearts. Ouchies! .-.")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));

            return false;
        }

        return true;
    }

    @Override
    public Class<ServerPlayerHealthPacket> getPacketClass()
    {
        return ServerPlayerHealthPacket.class;
    }
}
