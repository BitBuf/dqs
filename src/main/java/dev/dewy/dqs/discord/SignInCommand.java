package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class SignInCommand extends Command
{
    public SignInCommand()
    {
        this.name = "signin";
        this.help = "Sign into your Mojang account.";
        this.aliases = new String[] {"login"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
        this.arguments = "<EMAIL> <PASSWORD>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        String[] args = event.getArgs().split("\\s+");

        if (event.getArgs().isEmpty() || args.length != 2)
        {
            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Invalid Command Arguments")
                    .setDescription("You have entered invalid arguments for this command. Try again, like this:\n\n`" + Constants.CONFIG.discord.prefix + "signin " + this.arguments + "`")
                    .setColor(new Color(15221016))
                    .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                    .build());

            return;
        }

        if (DQS.getInstance().isConnected())
        {
            DQS.getInstance().getClient().getSession().disconnect("Account changeup! :o");
        }

        Constants.CONFIG.authentication.email = args[0];
        Constants.CONFIG.authentication.password = args[1];

        try
        {
            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Authentication")
                    .setDescription("Your Minecraft account details have been entered, encrypted and locked away. Attempting authentication...")
                    .setColor(new Color(10144497))
                    .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                    .build());
        } catch (MalformedURLException e)
        {
            Constants.DISCORD_LOG.error(e);

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
                            .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a sign in command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build()).queue()));
        }

        try
        {
            DQS.getInstance().start();
        } catch (LoginException e)
        {
            e.printStackTrace();
        }
    }
}
