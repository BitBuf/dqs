package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class AutoReconnectCommand extends Command
{
    public AutoReconnectCommand()
    {
        this.name = "autoreconnect";
        this.help = "Modify the auto reconnect delay";
        this.aliases = new String[] {"autorc", "arc"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
        this.arguments = "<seconds delay>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (event.getAuthor().getId().equals(Constants.CONFIG.discord.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.discord.operatorId) && (event.getChannel().getId().equals(Constants.CONFIG.discord.channelId) || !event.getMessage().getChannelType().isGuild()))
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (args.length > 1 || event.getArgs().isEmpty())
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You have entered invalid arguments for this command. Try again, like this:\n\n`" + Constants.CONFIG.discord.prefix + "autoreconnect " + this.arguments + "`")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                try
                {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You have not entered a valid integer for the auto reconnect delay. Try again, like this:\n\n`" + Constants.CONFIG.discord.prefix + "autoreconnect " + this.arguments + "`")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                if (Integer.parseInt(args[0]) <= 0)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("The DQS auto reconnect delay must be a positive integer above 0. Try again, like this:\n\n`" + Constants.CONFIG.discord.prefix + "autoreconnect " + this.arguments + "`")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    return;
                }

                Constants.CONFIG.modules.autoReconnect.delaySeconds = Integer.parseInt(args[0]);

                if (Integer.parseInt(args[0]) > 1)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Auto Reconnect")
                            .setDescription("You have set the auto reconnect delay to **" + Constants.CONFIG.modules.autoReconnect.delaySeconds + " seconds.**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());
                } else
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Auto Reconnect")
                            .setDescription("You have set the auto reconnect delay to **" + Constants.CONFIG.modules.autoReconnect.delaySeconds + " second.**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());
                }
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

                Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.subscriberId)).getName() + ")")
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of an autoreconnect command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}

