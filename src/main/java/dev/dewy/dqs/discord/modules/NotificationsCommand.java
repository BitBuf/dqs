package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class NotificationsCommand extends Command
{
    public NotificationsCommand()
    {
        this.name = "notifications";
        this.help = "Modify the auto reconnect delay";
        this.aliases = new String[] {"notifs", "dqswarn", "n", "notify"};
        this.guildOnly = false;
//        this.arguments = "<seconds delay>"; TODO: Can't be arsed
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
                    Constants.CONFIG.modules.notifications.enabled = !Constants.CONFIG.modules.notifications.enabled;

                    if (Constants.CONFIG.modules.notifications.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** the DQS notifications module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** the DQS notifications module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("player"))
                {
                    Constants.CONFIG.modules.notifications.playerInRange = !Constants.CONFIG.modules.notifications.playerInRange;

                    if (Constants.CONFIG.modules.notifications.playerInRange)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS player in range notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS player in range notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("crystal"))
                {
                    Constants.CONFIG.modules.notifications.crystalInRange = !Constants.CONFIG.modules.notifications.crystalInRange;

                    if (Constants.CONFIG.modules.notifications.crystalInRange)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS crystal in range notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS crystal in range notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
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
                                    .setDescription("You have not entered a valid integer for the queue notification threshold.")
                                    .setColor(new Color(15221016))
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                    .build());

                            return;
                        }

                        if (Integer.parseInt(args[1]) <= 5)
                        {
                            event.reply(new EmbedBuilder()
                                    .setTitle("**DQS** - Invalid Command Arguments")
                                    .setDescription("The DQS auto respawn delay must be a positive integer above 5. This is to ensure that you get the best value in DQS's service.")
                                    .setColor(new Color(15221016))
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                    .build());

                            return;
                        }

                        Constants.CONFIG.modules.notifications.threshold = Integer.parseInt(args[1]);

                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have set the DQS queue notification threshold to **" + Constants.CONFIG.modules.notifications.threshold + "**.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());

                        return;
                    }

                    Constants.CONFIG.modules.notifications.nearlyFinishedQueueing = !Constants.CONFIG.modules.notifications.nearlyFinishedQueueing;

                    if (Constants.CONFIG.modules.notifications.nearlyFinishedQueueing)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS queue notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS queue notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("relog"))
                {
                    Constants.CONFIG.modules.notifications.relogged = !Constants.CONFIG.modules.notifications.relogged;

                    if (Constants.CONFIG.modules.notifications.relogged)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS relog notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS relog notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("restart"))
                {
                    Constants.CONFIG.modules.notifications.serverMessages = !Constants.CONFIG.modules.notifications.serverMessages;

                    if (Constants.CONFIG.modules.notifications.serverMessages)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **enabled** DQS 2B2T server & restart notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Notifications")
                                .setDescription("You have **disabled** DQS 2B2T server & restart notifications.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Invalid Command Arguments")
                        .setDescription("Please enter a valid notification type to toggle.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .build());
            } catch (Throwable e)
            {
                Constants.DISCORD_LOG.error(e);

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Error")
                        .setDescription("An exception occurred whilst executing this command. Debug information has been sent to Dewy to be fixed in following updates. Sorry about any inconvenience!")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .build());

                Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.service.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.service.subscriberId)).getName() + ")")
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a notifications command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}
