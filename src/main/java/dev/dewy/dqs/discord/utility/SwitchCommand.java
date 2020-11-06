package dev.dewy.dqs.discord.utility;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.CONFIG;

public class SwitchCommand extends Command
{
    public SwitchCommand()
    {
        this.name = "switch";
        this.aliases = new String[] {"target", "sw", "srv"};
        this.guildOnly = false;
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (args.length > 2 || args.length < 1)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("Invalid arguments were entered for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features/#THING")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (DQS.getInstance().isConnected() || DQS.isRecon)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Switch")
                            .setDescription("Your account is currently connected or relogging. Use `&disconnect` to disconnect your account before switching servers.")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                if (args.length == 2)
                {
                    try
                    {
                        if (Integer.parseInt(args[1]) >= 0 && Integer.parseInt(args[1]) <= 65535)
                        {
                            CONFIG.client.server.port = Integer.parseInt(args[1]);
                        } else
                        {
                            event.reply(new EmbedBuilder()
                                    .setTitle("**DQS** - Invalid Port")
                                    .setDescription("The port number you entered (" + args[1] + ") is not a valid port. Port numbers must be within the range **0-65535**.")
                                    .setColor(new Color(15221016))
                                    .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                    .build());

                            return;
                        }
                    } catch (NumberFormatException e)
                    {
                        event.reply(new EmbedBuilder()
                                .setTitle("**DQS** - Invalid Integer")
                                .setDescription("The port number you entered (" + args[1] + ") is not a valid integer.")
                                .setColor(new Color(15221016))
                                .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build());

                        return;
                    }
                } else
                {
                    CONFIG.client.server.port = 25565;
                }

                CONFIG.client.server.address = args[0];

                Constants.SHOULD_RECONNECT = true;
                Constants.RATE_LIMITED = true;

                if (((DQSClientSession) DQS.getInstance().getClient().getSession()).isServerProbablyOff())
                {

                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Connecting")
                        .setDescription("Switching and connecting to new target server:\n\n`" + CONFIG.client.server.address + ":" + CONFIG.client.server.port + "`")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());

                new Thread(() ->
                {
                    try
                    {
                        if (DQS.getInstance().server != null)
                        {
                            DQS.getInstance().server.close();
                            DQS.getInstance().server = null;
                        }

                        DQS.getInstance().start();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Throwable e)
            {
                Constants.DISCORD_LOG.error(e);

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
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a connect command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}
