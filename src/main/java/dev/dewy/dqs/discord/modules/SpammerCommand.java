package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class SpammerCommand extends Command
{
    public SpammerCommand()
    {
        this.name = "spammer";
        this.help = "Configure the DQS spammer.";
        this.aliases = new String[] {"spam", "dqsspam", "chatspam", "chatspammer"};
        this.guildOnly = false;
        this.arguments = "[ADD / REMOVE / DELAY] [MESSAGE ID / SECONDS DELAY]";
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
                    Constants.CONFIG.modules.chatSpammer.enabled = !Constants.CONFIG.modules.chatSpammer.enabled;

                    if (Constants.CONFIG.modules.chatSpammer.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Spammer")
                                .setDescription("You have **enabled** the DQS spammer.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Spammer")
                                .setDescription("You have **disabled** the DQS spammer.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("add"))
                {
                    StringBuilder messageToAdd = new StringBuilder();

                    for (int i = 1; i < args.length; i++)
                    {
                        messageToAdd.append(" ").append(args[i]);
                    }

                    Constants.CONFIG.modules.chatSpammer.messages.add(messageToAdd.toString().substring(1));

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Spammer")
                            .setDescription("You have added this message to the DQS spammer messages list under **ID " + (Constants.CONFIG.modules.chatSpammer.messages.size() - 1) + "**...\n\n`" + messageToAdd.toString().substring(1) + "`")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    try
                    {
                        Integer.parseInt(args[1]);
                    } catch (NumberFormatException e)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("You have not entered a valid spammer message ID. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());

                        return;
                    }

                    Constants.CONFIG.modules.chatSpammer.messages.remove(Integer.parseInt(args[1]));

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Spammer")
                            .setDescription("You have removed the DQS spammer message with **ID " + args[1] + "**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                if (args[0].equalsIgnoreCase("delay"))
                {
                    try
                    {
                        Integer.parseInt(args[1]);
                    } catch (NumberFormatException e)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("You have not entered a valid integer for the chat spammer delay. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());

                        return;
                    }

                    if (Integer.parseInt(args[1]) <= 0)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("The DQS chat spammer delay must be a positive integer above 0. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());

                        return;
                    }

                    Constants.CONFIG.modules.chatSpammer.delaySeconds = Integer.parseInt(args[1]);

                    if (Integer.parseInt(args[1]) > 1)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Spammer")
                                .setDescription("You have set the DQS spammer delay to **" + Constants.CONFIG.modules.chatSpammer.delaySeconds + " seconds.**")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Spammer")
                                .setDescription("You have set the DQS spammer delay to **" + Constants.CONFIG.modules.chatSpammer.delaySeconds + " second.**")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("display"))
                {
                    if (Constants.CONFIG.modules.chatSpammer.messages.isEmpty())
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Spammer")
                                .setDescription("You do not have any registered DQS spam messages. Try `&spammer add` to add one.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build());

                        return;
                    }

                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < Constants.CONFIG.modules.chatSpammer.messages.size(); i++)
                    {
                        builder.append("**ID ").append(i).append(": **").append(Constants.CONFIG.modules.chatSpammer.messages.get(i)).append("\n");
                    }

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Spammer")
                            .setDescription("Your registered DQS spam messages: \n\n" + builder)
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Invalid Command Arguments")
                        .setDescription("You have entered invalid arguments for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .build());
            } catch (Throwable t)
            {
                Constants.DISCORD_LOG.error(t);

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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a spammer command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}
