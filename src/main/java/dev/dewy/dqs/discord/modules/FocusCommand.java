package dev.dewy.dqs.discord.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class FocusCommand extends Command
{
    public FocusCommand()
    {
        this.name = "focus";
        this.help = "Focus in on an account.";
        this.aliases = new String[] {"account", "acc", "fc"};
        this.guildOnly = false;
        this.arguments = "<NAME>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()))
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (Constants.CONFIG.modules.focus.focused && !Constants.CONFIG.modules.focus.enabled)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Focus")
                            .setDescription("You can not utilise the alts/focus module because you do not have any registered alts; please contact Dewy to add one.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (args[0].isEmpty())
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("Please enter an account to potentially focus on.")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (Constants.CONFIG.modules.focus.focused && !Constants.CONFIG.modules.focus.accounts.contains(args[0]))
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Focus")
                            .setDescription("You tried to focus on the account **" + args[0] + "**, but it could not be found under the list of registered accounts. Typo?")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (!Constants.CONFIG.modules.focus.focused && args[0].equalsIgnoreCase(Constants.CONFIG.authentication.username))
                {
                    Constants.CONFIG.modules.focus.focused = true;

                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Focus")
                            .setDescription("You have focused on the account **\"" + Constants.CONFIG.authentication.username + "\"**.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                Constants.CONFIG.modules.focus.focused = false;
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a focus command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
