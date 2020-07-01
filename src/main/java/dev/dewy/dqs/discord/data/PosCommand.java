package dev.dewy.dqs.discord.data;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class PosCommand extends Command
{
    public PosCommand()
    {
        this.name = "pos";
        this.help = "View your position in queue.";
        this.aliases = new String[] {"qpos", "position", "queuepos"};
        this.guildOnly = false;
    }

    private static int getNormalQueueLength()
    {
        try
        {
            String data = getDataFrom("https://2b2t.io/api/queue?last=true");

            return Integer.parseInt(data.replace("[", "").replace("]", "").split(",")[1]);
        } catch (Throwable t)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                            .setDescription("The 2b2t.io API may be down for normal queue. Notify the public.")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));

            return -1;
        }
    }

    private static int getPrioQueueLength()
    {
        try
        {
            String data = getDataFrom("https://2b2t.io/api/prioqueue?last=true");

            return Integer.parseInt(data.replace("[", "").replace("]", "").split(",")[1]);
        } catch (Throwable t)
        {
            Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(Constants.DISCORD.getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                            .setDescription("The 2b2t.io API may be down for PrioQ specifically. Notify the public.")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build()).queue()));

            return -1;
        }
    }

    private static String getDataFrom(String url)
    {
        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            HttpClient client = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                if (!Constants.CONFIG.client.server.address.equalsIgnoreCase("2b2t.org"))
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Not Set To 2B2T")
                            .setDescription("A queue position could not be determined because your target server is not set to **2b2t.org**, it is currently set to **" + Constants.CONFIG.client.server.address + "**.")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (DQS.placeInQueue != -1)
                {
                    if (Integer.parseInt(DQS.getPosition()) > getNormalQueueLength())
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Queue Position & Statistics")
                                .setDescription("Your current queue position: **" + DQS.getPosition() + "**\n\nNormal Queue Length: **" + getNormalQueueLength() + "**\nPriority Queue Length: **" + getPrioQueueLength() + "**\n\n**Disclaimer:** The 2B2T API hasn't updated in the last few minutes, so queue length data may be inaccurate.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Queue Position & Statistics")
                            .setDescription("Your current queue position: **" + DQS.getPosition() + "**\n\nNormal Queue Length: **" + getNormalQueueLength() + "**\nPriority Queue Length: **" + getPrioQueueLength() + "**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());
                } else
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Queue Position")
                            .setDescription("Your account is currently in-game, not in the queue.\n\n*PLEASE NOTE: if your instance has only just started, this is likely a lie.*")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());
                }
            } catch (Exception e)
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Error")
                        .setDescription("An exception occurred whilst executing this command. Debug information has been sent to Dewy to be fixed in following updates. Sorry about any inconvenience!")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());

                Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a queue pos command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}

