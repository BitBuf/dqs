package dev.dewy.dqs.client.handler.incoming.spawn;

import com.google.gson.JsonParser;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.cache.data.entity.EntityPlayer;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.DISCORD;

public class SpawnPlayerHandler implements HandlerRegistry.IncomingHandler<ServerSpawnPlayerPacket, DQSClientSession>
{
    public static String getNameFromUUID(String uuid)
    {
        try
        {
            String jsonUrl = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));
            JsonParser parser = new JsonParser();

            return parser.parse(jsonUrl).getAsJsonArray().get(parser.parse(jsonUrl).getAsJsonArray().size() - 1).getAsJsonObject().get("name").toString();
        } catch (IOException ex)
        {
            Constants.MODULE_LOG.error(ex);

            Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                            .setDescription("A " + ex.getClass().getSimpleName() + " was thrown whilst getting the name from the UUID in a player spawn warning.\n\n**Cause:**\n\n```" + ex.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));
        }

        return null;
    }

    @Override
    public boolean apply(ServerSpawnPlayerPacket packet, DQSClientSession session)
    {
        CACHE.getEntityCache().add(new EntityPlayer()
                .setEntityId(packet.getEntityId())
                .setUuid(packet.getUUID())
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch())
                .setMetadata(Arrays.asList(packet.getMetadata()))
        );

        try
        {
            if (Constants.CONFIG.modules.notifications.enabled && Constants.CONFIG.modules.notifications.playerInRange && DQS.getInstance().isConnected() && !DQS.getInstance().connectedToProxy)
            {
                Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                {
                    try
                    {
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Player In Range")
                                .setDescription("A player by the name of **" + getNameFromUUID(packet.getUUID().toString()) + "** has entered your account's visible range.")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .setFooter("Notification intended for  " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .build()).queue();
                    } catch (MalformedURLException e)
                    {
                        Constants.DISCORD_LOG.error(e);
                    }
                }));
            }
        } catch (Throwable t)
        {
            Constants.DISCORD_LOG.error(t);

            Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a player in range notification command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));
        }

        if (Constants.CONFIG.modules.autoDisconnect.enabled && Constants.CONFIG.modules.autoDisconnect.playerInRange && DQS.getInstance().isConnected() && !DQS.getInstance().connectedToProxy)
        {
            session.getDqs().getClient().getSession().disconnect("§7[§b§lDQS§r§7] §fAuto disconnect.", false);
        }

        return true;
    }

    @Override
    public Class<ServerSpawnPlayerPacket> getPacketClass()
    {
        return ServerSpawnPlayerPacket.class;
    }
}
