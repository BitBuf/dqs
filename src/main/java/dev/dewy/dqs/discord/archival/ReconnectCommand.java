package dev.dewy.dqs.discord.archival;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.CONFIG;
import static dev.dewy.dqs.utils.Constants.saveConfig;

public class ReconnectCommand extends Command
{
    public ReconnectCommand()
    {
        this.name = "reconnect";
        this.help = "Reconnect your account to the target server.";
        this.aliases = new String[] {"connect", "rcon"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                if (!DQS.getInstance().isConnected())
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Reconnect")
                            .setDescription("Attempting reconnection to **" + CONFIG.client.server.address + "**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    Constants.SHOULD_RECONNECT = true;

                    DQS.getInstance().logIn();
                    DQS.getInstance().connect();

                    DQS.getInstance().server = null;
                    DQS.getInstance().startServer();

                    saveConfig();

                    DQS.placeInQueue = -1;
                    DQS.startTime = -1;
                    DQS.startPosition = -1;

                    saveConfig();

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Reconnect")
                        .setDescription("Your account could not be reconnected because it is already connected to the server. Try `&disconnect` to disconnect.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());
            } catch (Throwable t)
            {
                Constants.DISCORD_LOG.error(t);

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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a reconnect command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
