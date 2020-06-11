package dev.dewy.dqs.discord.dewy;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.WEBSOCKET_SERVER;
import static dev.dewy.dqs.utils.Constants.saveConfig;

public class KillCommand extends Command
{
    public KillCommand()
    {
        this.name = "kill";
        this.help = "Stops the instance.";
        this.aliases = new String[] {"terminate", "term"};
        this.ownerCommand = true;
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId) && event.getChannel().getId().equals(Constants.CONFIG.service.channelId))
        {
            try
            {
                if (event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId))
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Instance Termination")
                            .setDescription("Shutting down instance for " + event.getJDA().getUserById(Constants.CONFIG.service.subscriberId).getName() + "...")
                            .setColor(new Color(10144497))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                            .build());

                    Thread.sleep(5000L);

                    if (DQS.getInstance().server != null)
                    {
                        DQS.getInstance().server.close(true);
                    }

                    WEBSOCKET_SERVER.shutdown();
                    saveConfig();

                    Runtime.getRuntime().exit(0);
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Insufficient Permissions")
                        .setDescription("This command can only be executed by an operator.")
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a kill command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}
