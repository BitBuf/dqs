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
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Modules")
                        .setDescription("An overview of your active modules and their configurations is displayed below.")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .addField("**Auto Reconnect**", "**Delay:** " + Constants.CONFIG.modules.autoReconnect.delaySeconds, true)
                        .addField("**Auto Disconnect**", "**Player In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoDisconnect.playerInRange) + "\n**Crystal In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoDisconnect.crystalInRange) + "\n\n**Nearly Finished Queueing:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoDisconnect.nearlyFinishedQueueing) + "\n**Queue Threshold:** " + Constants.CONFIG.modules.autoDisconnect.nearlyFinishedQueueingThreshold + "\n\n**Low HP:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoDisconnect.lowHp) + "\n**Low HP Threshold:** " + Constants.CONFIG.modules.autoDisconnect.lowHpThreshold, true)
                        .addField("**Auto Respawn**", "**Enabled:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.autoRespawn.enabled) + "\n**Delay:** " + Constants.CONFIG.modules.autoRespawn.delaySeconds, true)
                        .addField("**Notifications**", "**Player In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.playerInRange) + "\n**Crystal In Range:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.crystalInRange) + "\n\n**Nearly Finished Queueing:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.nearlyFinishedQueueing) + "\n**Threshold:** " + Constants.CONFIG.modules.notifications.threshold + "\n\n**Relog:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.relogged) + "\n**Server Messages:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.notifications.serverMessages), true)
                        .addField("**Focus**", "**Focused Account:** " + Constants.CONFIG.authentication.username + "\n**Refocusable:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.focus.enabled), true)
                        .addField("**Whitelist**", "**Whitelist Active:** " + InfoCommand.getEmojiFromBoolean(Constants.CONFIG.modules.whitelist.enabled), true)
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a modules command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
