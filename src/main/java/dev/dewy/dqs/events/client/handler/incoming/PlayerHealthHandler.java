package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.client.ClientRequestPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
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
        CACHE_LOG.info("Player food: %d", packet.getFood())
                .info("Player saturation: %f", packet.getSaturation()) // switch back to debug later
                .info("Player health: %f", packet.getHealth());
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

        if (packet.getHealth() <= CONFIG.modules.autoDisconnect.lowHpThreshold && CONFIG.modules.autoDisconnect.enabled && CONFIG.modules.autoDisconnect.lowHp && !DQS.getInstance().connectedToProxy && !DQS.hasHpWarned)
        {
            DQS.getInstance().getClient().getSession().disconnect("user disconnect", false);

            try
            {
                Objects.requireNonNull(DISCORD.getUserById(CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You were automatically disconnected and **saved** due to your health reaching " + (Math.round(packet.getHealth() * 2.0F) / 2.0F) + " hearts. Ouchies! .-.")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            } catch (Throwable t)
            {
                DISCORD_LOG.alert(t);
            }

            DQS.hasHpWarned = true;

            return false;
        }

        if (packet.getHealth() <= CONFIG.modules.notifications.lowHpThreshold && CONFIG.modules.notifications.enabled && CONFIG.modules.notifications.lowHp && !DQS.getInstance().connectedToProxy && !DQS.hasHpWarned)
        {
            try
            {
                Objects.requireNonNull(DISCORD.getUserById(CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Low HP Notification")
                                .setDescription("Low HP warning. Your health has just reached " + (Math.round(packet.getHealth() * 2.0F) / 2.0F) + " hearts.")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            } catch (Throwable t)
            {
                DISCORD_LOG.alert(t);
            }

            DQS.hasHpWarned = true;
        }

        return true;
    }

    @Override
    public Class<ServerPlayerHealthPacket> getPacketClass()
    {
        return ServerPlayerHealthPacket.class;
    }
}
