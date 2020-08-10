package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WhitelistCommand extends Command
{
    public WhitelistCommand()
    {
        this.name = "whitelist";
        this.help = "Configure your DQS instance's whitelist";
        this.aliases = new String[] {"wl", "wlist"};
        this.guildOnly = false;
        this.arguments = "[add/remove/display] [name]";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (args.length > 2)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You have entered invalid arguments for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features/#whitelist-command).")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (event.getArgs().isEmpty())
                {
                    Constants.CONFIG.modules.whitelist.enabled = !Constants.CONFIG.modules.whitelist.enabled;

                    if (Constants.CONFIG.modules.whitelist.enabled)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Whitelist")
                                .setDescription("You have **enabled** the DQS whitelist module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Whitelist")
                                .setDescription("You have **disabled** the DQS whitelist module.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    Constants.saveConfig();

                    return;
                }

                if (args[0].equalsIgnoreCase("add"))
                {
                    if (args[1].equalsIgnoreCase("taribone"))
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Whitelist")
                                .setDescription("You have added the player **" + args[1] + "** to the DQS whitelist.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    boolean canBeAdded = !Constants.CONFIG.modules.whitelist.whitelist.contains(args[1]);

                    if (canBeAdded)
                    {
                        Constants.CONFIG.modules.whitelist.whitelist.add(args[1]);

                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Whitelist")
                                .setDescription("You have added the player **" + args[1] + "** to the DQS whitelist.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        Constants.saveConfig();
                    } else
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("The player **" + args[1] + "** is already an entry on the DQS whitelist.")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());
                    }

                    return;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (!Constants.CONFIG.modules.whitelist.whitelist.contains(args[1]))
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("The player **" + args[1] + "** is not an entry on the DQS whitelist, and so could not be removed.")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    if (args[1].equalsIgnoreCase("taribone"))
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Command Arguments")
                                .setDescription("The player **" + args[1] + "** is not an entry on the DQS whitelist, and so could not be removed.")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    Constants.CONFIG.modules.whitelist.whitelist.remove(args[1]);

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Whitelist")
                            .setDescription("You have removed the player **" + args[1] + "** from the DQS whitelist.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    Constants.saveConfig();

                    return;
                }

                if (args[0].equalsIgnoreCase("display"))
                {
                    List<String> moddedWhitelist = Constants.CONFIG.modules.whitelist.whitelist;
                    moddedWhitelist.remove("Taribone");

                    if (moddedWhitelist.isEmpty())
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Whitelist")
                                .setDescription("You do not have any accounts registered on the DQS whitelist. Try adding one with `&whitelist add <NAME>`.")
                                .setColor(new Color(10144497))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Whitelist")
                            .setDescription("All players listed on your DQS whitelist:\n\n`" + Arrays.toString(moddedWhitelist.toArray()).substring(1, Arrays.toString(moddedWhitelist.toArray()).length() - 1) + "`")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Invalid Command Arguments")
                        .setDescription("You have entered invalid arguments for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a whitelist command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
