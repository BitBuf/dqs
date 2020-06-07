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

        DQS.getInstance().inQueue = "2b2t.org".equalsIgnoreCase(CONFIG.client.server.address)
                && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Position in queue".toLowerCase());

        if ("2b2t.org".equalsIgnoreCase(CONFIG.client.server.address) && !MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Position in queue".toLowerCase()))
        {
            DQS.getInstance().inQueue = false;
            DQS.getInstance().currentPos = -1;
        }

        if (DQS.getInstance().inQueue)
        {
            String[] result = MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().split("(?=\\d*$)", 2);

            DQS.getInstance().currentPos = Integer.parseInt(result[1]);
        }

        if ("2b2t.org".equalsIgnoreCase(CONFIG.client.server.address)
                && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Exception Connecting:".toLowerCase()))
        {
            CLIENT_LOG.error("2b2t's queue is broken as per usual, disconnecting to avoid being stuck forever");
            session.disconnect("§7[§b§lDQS§r§7] §fAntiStuck Disconnect");
        }

        if ("2b2t.org".equalsIgnoreCase(CONFIG.client.server.address) && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("[SERVER]".toLowerCase()) && CONFIG.modules.notifications.serverMessages && CONFIG.modules.notifications.enabled)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.discord.subscriberId)).openPrivateChannel().queue((privateChannel ->
            {
                try
                {
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Server Notification")
                            .setDescription(MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString())
                            .setColor(new Color(10144497))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .build()).queue();
                } catch (MalformedURLException e)
                {
                    Constants.DISCORD_LOG.error(e);
                }
            }));
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
