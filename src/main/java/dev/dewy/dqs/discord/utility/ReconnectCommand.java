package dev.dewy.dqs.discord.utility;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.saveConfig;

public class ReconnectCommand extends Command
{
    public ReconnectCommand()
    {
        this.name = "reconnect";
        this.help = "Reconnect your account to the target server.";
        this.aliases = new String[] {"connect", "rcon"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            if (!DQS.getInstance().isConnected())
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Reconnect")
                        .setDescription("Your account has been disconnected from the server. Please use `&reconnect` to reconnect to " + Constants.CONFIG.client.server.address)
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                        .build());

                Constants.SHOULD_RECONNECT = true;

                DQS.getInstance().logIn();
                DQS.getInstance().connect();
                DQS.getInstance().server = null;

                saveConfig();

                return;
            }

            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Reconnect")
                    .setDescription("Your account could not be reconnected because it is already connected to the server. Try `&disconnect` to disconnect.")
                    .setColor(new Color(15221016))
                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
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
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a reconnect command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build()).queue()));
        }
    }
}
