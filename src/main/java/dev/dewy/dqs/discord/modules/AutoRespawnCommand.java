package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class AutoRespawnCommand extends Command
{
    public AutoRespawnCommand()
    {
        this.name = "autorespawn";
        this.help = "Configure the DQS auto respawn module.";
        this.aliases = new String[] {"autors", "respawn", "spawn", "ars"};
        this.guildOnly = false;
        this.arguments = "[seconds delay]";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (args.length > 1)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You have entered invalid arguments for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (event.getArgs().isEmpty())
                {
                    Constants.CONFIG.modules.autoRespawn.enabled = !Constants.CONFIG.modules.autoRespawn.enabled;

                    if (Constants.CONFIG.modules.autoRespawn.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Respawn")
                                .setDescription("You have **enabled** the DQS auto respawn module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Auto Respawn")
                                .setDescription("You have **disabled** the DQS auto respawn module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                try
                {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Integer")
                            .setDescription("You have not entered a valid integer for the auto respawn delay. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (Integer.parseInt(args[0]) <= 0)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Inapplicable Integer")
                            .setDescription("The DQS auto respawn delay must be a positive integer above 0. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                Constants.CONFIG.modules.autoRespawn.delaySeconds = Integer.parseInt(args[0]);

                if (Integer.parseInt(args[0]) > 1)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Auto Respawn")
                            .setDescription("You have set the auto respawn delay to **" + Constants.CONFIG.modules.autoRespawn.delaySeconds + " seconds.**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());
                } else
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Auto Respawn")
                            .setDescription("You have set the auto respawn delay to **" + Constants.CONFIG.modules.autoRespawn.delaySeconds + " second.**")
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of an auto respawn command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
