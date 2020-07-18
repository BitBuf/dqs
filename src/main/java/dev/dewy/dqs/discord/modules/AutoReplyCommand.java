package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class AutoReplyCommand extends Command
{
    public AutoReplyCommand()
    {
        this.name = "autoreply";
        this.help = "Configure the DQS Auto Reply module.";
        this.aliases = new String[] {"arp", "reply"};
        this.guildOnly = false;
        this.arguments = "[MESSAGE]";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                if (event.getArgs().isEmpty())
                {
                    Constants.CONFIG.modules.autoReply.enabled = !Constants.CONFIG.modules.autoReply.enabled;

                    if (Constants.CONFIG.modules.autoReply.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Reply")
                                .setDescription("You have **enabled** the DQS auto reply module.\n\n**Message:** " + Constants.CONFIG.modules.autoReply.message + "**")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Chat Relay")
                                .setDescription("You have **disabled** the DQS auto reply module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                String[] args = event.getArgs().split("\\s+");
                StringBuilder message = new StringBuilder();

                for (int i = 1; i < args.length; i++)
                {
                    message.append(" ").append(args[i]);
                }

                if (message.length() >= 255)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You can not set an auto reply message larger than 255 characters. Please try and shorten your message.")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                Constants.CONFIG.modules.autoReply.message = message.toString();

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Auto Reply")
                        .setDescription("You have set your DQS auto reply message to be:\n\n`" + message.toString() + "`")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of an auto reply command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
