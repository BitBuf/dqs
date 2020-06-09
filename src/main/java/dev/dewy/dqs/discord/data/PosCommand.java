package dev.dewy.dqs.discord.data;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class PosCommand extends Command
{
    public PosCommand()
    {
        this.name = "pos";
        this.help = "View your position in queue.";
        this.aliases = new String[] {"qpos", "position"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (event.getAuthor().getId().equals(Constants.CONFIG.discord.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.discord.operatorId) && (event.getChannel().getId().equals(Constants.CONFIG.discord.channelId) || !event.getMessage().getChannelType().isGuild()))
        {
            try
            {
                if (DQS.getInstance().inQueue && DQS.getInstance().currentPos != -1)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Queue Position")
                            .setDescription("Your current queue position: **" + DQS.getInstance().currentPos + "**")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build());
                } else if (DQS.getInstance().inQueue)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Queue Position")
                            .setDescription("Please wait a few seconds before querying your queue position.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build());
                } else
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Queue Position")
                            .setDescription("Your account is currently in-game, not in the queue.")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build());
                }
            } catch (Exception e)
            {
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
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a queue pos command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                                .build()).queue()));
            }
        }
    }
}
