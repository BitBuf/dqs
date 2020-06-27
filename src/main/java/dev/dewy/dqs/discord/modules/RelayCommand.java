package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class RelayCommand extends Command
{
    public RelayCommand()
    {
        this.name = "relay";
        this.help = "Configure the DQS auto respawn module.";
        this.aliases = new String[] {"chatrelay", "lolritter", "cr"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                Constants.CONFIG.modules.chatRelay.enabled = !Constants.CONFIG.modules.chatRelay.enabled;

                if (Constants.CONFIG.modules.chatRelay.enabled)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Chat Relay")
                            .setDescription("You have **enabled** the DQS chat relay module.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());
                } else
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Chat Relay")
                            .setDescription("You have **disabled** the DQS chat relay module.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());
                }
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a relay command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
