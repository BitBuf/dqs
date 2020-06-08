package dev.dewy.dqs.discord.data;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class ModulesCommand extends Command
{
    public ModulesCommand()
    {
        this.name = "modules";
        this.help = "Displays information about the status and configuration of modules.";
        this.aliases = new String[] {"m", "mods", "features"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Modules")
                    .setDescription("An overview of your active modules and their configurations is displayed below.")
                    .setColor(new Color(10144497))
                    .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                    .addField("**Auto Reconnect**", "**Delay:** " + Constants.CONFIG.modules.autoReconnect.delaySeconds, true)
                    .addField("**Auto Respawn**", "**Enabled:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoRespawn.enabled) + "\n**Delay:** " + Constants.CONFIG.modules.autoRespawn.delaySeconds, true)
                    .addField("**Notifications**", "**Player In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.playerInRange) + "\n**Crystal In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.crystalInRange) + "\n\n**Nearly Finished Queueing:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.nearlyFinishedQueueing) + "\n**Threshold:** " + Constants.CONFIG.modules.notifications.threshold + "\n\n**Relog:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.relogged) + "\n**Server Messages:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.serverMessages), true)
                    .build());
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
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a modules command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build()).queue()));
        }
    }
}
