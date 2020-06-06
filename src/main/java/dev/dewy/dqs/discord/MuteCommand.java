package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class MuteCommand extends Command
{
    public MuteCommand()
    {
        this.name = "mute";
        this.help = "Mutes / unmutes all DQS notifications and messages.";
        this.aliases = new String[] {"silence"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            Constants.CONFIG.discord.muted = !Constants.CONFIG.discord.muted;

            if (Constants.CONFIG.discord.muted)
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Muted")
                        .setDescription("You have muted DQS. All notifications and messages won't be sent, no matter the level of importance.\n\nSee the [documentation](https://dewy.dev/dqs/features/muting) for DQS muting for more information.")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                        .build());
            } else
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Unmuted")
                        .setDescription("You have umuted DQS. Notifications and messages will be sent as normal.\n\nSee the [documentation](https://dewy.dev/dqs/features/muting) for DQS muting for more information.")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
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
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                    .build());

            Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.operatorId)).openPrivateChannel().queue((privateChannel ->
                    privateChannel.sendMessage(new EmbedBuilder()
                            .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.subscriberId)).getName() + ")")
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a mute command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build()).queue()));
        }
    }
}
