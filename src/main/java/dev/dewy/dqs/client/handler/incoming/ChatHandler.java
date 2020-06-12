package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerChatPacket;
import dev.dewy.dqs.utils.Constants;
import net.daporkchop.lib.minecraft.text.parser.MCFormatParser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.*;

public class ChatHandler implements HandlerRegistry.IncomingHandler<ServerChatPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerChatPacket packet, DQSClientSession session)
    {
        CHAT_LOG.info(packet.getMessage());

//        DQS.getInstance().inQueue = "2b2t.org".equalsIgnoreCase(CONFIG.client.server.address)
//                && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Position in queue".toLowerCase());

        if (MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().startsWith("<"))
        {
            DQS.placeInQueue = -1;
            DQS.startTime = -1;
            DQS.startPosition = -1;

            DQS.queueNotifArmed = true;
        }

        if (CONFIG.modules.chatRelay.enabled && CONFIG.modules.focus.focused)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
            {
                try
                {
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Chat Relay")
                            .setDescription(MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString())
                            .setColor(new Color(10144497))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .setFooter("Chat relay intended for " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .build()).queue();
                } catch (MalformedURLException e)
                {
                    Constants.DISCORD_LOG.error(e);
                }
            }));
        }

        if (MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().matches("^.* whispers: .*$") && !DQS.getInstance().isConnected())
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
            {
                try
                {
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Message Relay")
                            .setDescription(MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString())
                            .setColor(new Color(10144497))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .setFooter("Message relay intended for " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .build()).queue();
                } catch (MalformedURLException e)
                {
                    Constants.DISCORD_LOG.error(e);
                }
            }));
        }

        if (MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().startsWith("Position in queue: "))
        {
            DQS.placeInQueue = Integer.parseInt(MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().split("Position in queue: ") [1]);

            if (DQS.startPosition == -1 && DQS.startTime == -1)
            {
                DQS.startPosition = DQS.placeInQueue;
                DQS.startTime = System.nanoTime();
            }
        }

        if (DQS.placeInQueue <= CONFIG.modules.notifications.threshold && DQS.placeInQueue > 0 && DQS.queueNotifArmed && CONFIG.modules.notifications.enabled &&  CONFIG.modules.notifications.nearlyFinishedQueueing)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
            {
                try
                {
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Almost Finished Queueing")
                            .setDescription("The account **" + CONFIG.authentication.username + "** has almost finished queueing, at queue position " + DQS.placeInQueue + ".")
                            .setColor(new Color(10144497))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .setFooter("Notification intended for  " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .build()).queue();

                    DQS.queueNotifArmed = false;
                } catch (MalformedURLException e)
                {
                    Constants.DISCORD_LOG.error(e);
                }
            }));
        }

        if ("2b2t.org".equalsIgnoreCase(CONFIG.client.server.address)
                && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Exception Connecting:".toLowerCase()))
        {
            CLIENT_LOG.error("2b2t's queue is broken as per usual, disconnecting to avoid being stuck forever");
            session.disconnect("§7[§b§lDQS§r§7] §fAntiStuck Disconnect");
        }

        if ("2b2t.org".equalsIgnoreCase(CONFIG.client.server.address) && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("[SERVER]".toLowerCase()) && CONFIG.modules.notifications.serverMessages && CONFIG.modules.notifications.enabled && CONFIG.modules.focus.focused)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
            {
                try
                {
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Server Notification")
                            .setDescription(MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString())
                            .setColor(new Color(10144497))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .build()).queue();
                } catch (MalformedURLException e)
                {
                    Constants.DISCORD_LOG.error(e);
                }
            }));
        }

        if (MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().contains("whispers"))
        {
            Objects.requireNonNull(DISCORD.getUserById(CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Message Received")
                            .setDescription("You received a message:\n\n`" + MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString() + "`")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build()).queue()));
        }

        WEBSOCKET_SERVER.fireChat(packet.getMessage());
        return true;
    }

    @Override
    public Class<ServerChatPacket> getPacketClass()
    {
        return ServerChatPacket.class;
    }
}
