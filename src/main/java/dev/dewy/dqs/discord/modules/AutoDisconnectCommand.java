package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class AutoDisconnectCommand extends Command
{
    public AutoDisconnectCommand()
    {
        this.name = "autodisconnect";
        this.help = "Modify the auto reconnect delay";
        this.aliases = new String[] {"autodc", "adc"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (event.getArgs().isEmpty())
                {
                    Constants.CONFIG.modules.autoDisconnect.enabled = !Constants.CONFIG.modules.autoDisconnect.enabled;

                    if (Constants.CONFIG.modules.autoDisconnect.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **enabled** the DQS auto disconnect module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **disabled** the DQS auto disconnect module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("player"))
                {
                    Constants.CONFIG.modules.autoDisconnect.playerInRange = !Constants.CONFIG.modules.autoDisconnect.playerInRange;

                    if (Constants.CONFIG.modules.autoDisconnect.playerInRange)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **enabled** auto reconnect for when a player comes into range.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **disabled** auto reconnect for when a player comes into range.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("crystal"))
                {
                    Constants.CONFIG.modules.autoDisconnect.crystalInRange = !Constants.CONFIG.modules.autoDisconnect.crystalInRange;

                    if (Constants.CONFIG.modules.autoDisconnect.crystalInRange)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **enabled** auto reconnect for when an Ender Crystal comes into range.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Disconnect")
                                .setDescription("You have **disabled** auto reconnect for when an Ender Crystal comes into range.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("queue"))
                {
                    if (args.length == 2)
                    {
                        try
                        {
                            Integer.parseInt(args[1]);
                        } catch (NumberFormatException e)
                        {
                            event.reply(new EmbedBuilder()
                                    .setTitle("**DQS** - Invalid Command Arguments")
                                    .setDescription("You have not entered a valid integer for the queue auto relog threshold.")
                                    .setColor(new Color(15221016))
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                    .build());

                            return;
                        }

                        if (Integer.parseInt(args[1]) <= 5)
                        {
                            event.reply(new EmbedBuilder()
                                    .setTitle("**DQS** - Invalid Command Arguments")
                                    .setDescription("The DQS auto queue relog delay must be a positive integer above 5. This is to ensure that you get the best value in DQS's service.")
                                    .setColor(new Color(15221016))
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                    .build());

                            return;
                        }

                        Constants.CONFIG.modules.autoDisconnect.threshold = Integer.parseInt(args[1]);

                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have set the DQS queue auto relog threshold to **" + Constants.CONFIG.modules.autoDisconnect.threshold + "**.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    Constants.CONFIG.modules.autoDisconnect.nearlyFinishedQueueing = !Constants.CONFIG.modules.autoDisconnect.nearlyFinishedQueueing;

                    if (Constants.CONFIG.modules.autoDisconnect.nearlyFinishedQueueing)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS queue auto relog.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS queue auto relog.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Invalid Command Arguments")
                        .setDescription("Please enter a valid auto disconnect type to toggle.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());
            } catch (Throwable e)
            {
                Constants.DISCORD_LOG.error(e);

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
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of an auto disconnect command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
