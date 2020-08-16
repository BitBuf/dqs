package dev.dewy.dqs.client.handler.incoming.spawn;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.cache.data.entity.EntityObject;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import dev.dewy.dqs.protocol.game.entity.type.object.ObjectType;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.*;

public class SpawnObjectHandler implements HandlerRegistry.IncomingHandler<ServerSpawnObjectPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSpawnObjectPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityObject()
                .setObjectType(packet.getType())
                .setData(packet.getData())
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch())
                .setVelX(packet.getMotionX())
                .setVelY(packet.getMotionY())
                .setVelZ(packet.getMotionZ())
        );

        try
        {
            if (Constants.CONFIG.modules.notifications.enabled && Constants.CONFIG.modules.notifications.crystalInRange && DQS.getInstance().isConnected() && packet.getType().equals(ObjectType.ENDER_CRYSTAL) && !DQS.getInstance().connectedToProxy)
            {
                try
                {
                    Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                    {
                        try
                        {
                            privateChannel.sendMessage(new EmbedBuilder()
                                    .setTitle("**DQS** - Crystal In Range")
                                    .setDescription("An Ender Crystal has appeared in your account's visible range.")
                                    .setColor(new Color(15221016))
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                    .setFooter("Notification intended for " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                    .build()).queue();
                        } catch (MalformedURLException e)
                        {
                            Constants.DISCORD_LOG.error(e);
                        }
                    }));
                } catch (Throwable t)
                {
                    DISCORD_LOG.alert(t);
                }

                try
                {
                    Thread.sleep(2000L);
                } catch (InterruptedException e)
                {
                    DEFAULT_LOG.alert(e);
                }
            }

            if (Constants.CONFIG.modules.autoDisconnect.enabled && Constants.CONFIG.modules.autoDisconnect.crystalInRange && DQS.getInstance().isConnected() && packet.getType().equals(ObjectType.ENDER_CRYSTAL) && !DQS.getInstance().connectedToProxy)
            {
                session.getDqs().getClient().getSession().disconnect("§7[§b§lDQS§r§7] §fAuto disconnect.", false);

                try
                {
                    Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                    {
                        try
                        {
                            privateChannel.sendMessage(new EmbedBuilder()
                                    .setTitle("**DQS** - Crystal In Range")
                                    .setDescription("You were disconnected because an Ender Crystal has entered your accounts visible range.")
                                    .setColor(new Color(15221016))
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                    .build()).queue();
                        } catch (MalformedURLException e)
                        {
                            Constants.DISCORD_LOG.error(e);
                        }
                    }));
                } catch (Throwable t)
                {
                    DISCORD_LOG.alert(t);
                }

                try
                {
                    Thread.sleep(3000L);
                } catch (InterruptedException e)
                {
                    DEFAULT_LOG.alert(e);
                }
            }
        } catch (Throwable t)
        {
            Constants.DISCORD_LOG.error(t);

            Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a crystal in range notification command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));
        }

        return true;
    }

    @Override
    public Class<ServerSpawnObjectPacket> getPacketClass()
    {
        return ServerSpawnObjectPacket.class;
    }
}
