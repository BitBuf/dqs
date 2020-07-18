package dev.dewy.dqs.discord.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Objects;

import static dev.dewy.dqs.utils.Constants.*;

public class SignInCommand extends Command
{
    public SignInCommand()
    {
        this.name = "signin";
        this.help = "Sign into your Mojang account.";
        this.aliases = new String[] {"login"};
        this.guildOnly = false;
        this.arguments = "<IGN> <EMAIL> <PASSWORD>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                String[] args = event.getArgs().split("\\s+");

                if (event.getArgs().isEmpty() || args.length != 3)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Invalid Command Arguments")
                            .setDescription("You have entered invalid arguments for this command. Why not take a look at the documentation, [here](https://dqs.dewy.dev/features).")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                Constants.CONFIG.authentication.username = args[0];
                Constants.CONFIG.authentication.email = args[1];
                Constants.CONFIG.authentication.password = args[2];

                CONFIG.authentication.uuid = getUUIDFromName(CONFIG.authentication.username, true, true);

                CONFIG.authentication.isRateLimit = false;

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Authentication")
                        .setDescription("Your Minecraft account details have been entered, encrypted and locked away. Attempting authentication...")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());

                if (DQS.getInstance().isConnected())
                {
                    DQS.getInstance().getClient().getSession().disconnect("user disconnect", false);
                } else
                {
                    CACHE.reset(true);

                    DQS.getInstance().logIn();
                    DQS.getInstance().connect();

                    DQS.placeInQueue = -1;
                    DQS.startTime = -1;
                    DQS.startPosition = -1;

                    saveConfig();
                }
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a sign in command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }

    public static String getUUIDFromName(final String name, final boolean onlinemode, final boolean withSeperators)
    {
        String uuid;

        if (onlinemode)
        {
            try
            {
                final BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
                uuid = ((JsonObject)new JsonParser().parse(in)).get("id").toString().replaceAll("\"", "");

                in.close();
            }
            catch (Exception e)
            {
                uuid = null;
            }
        }
        else
        {
            uuid = null;
        }

        if (uuid != null)
        {
            if (withSeperators)
            {
                if (!uuid.contains("-"))
                {
                    return uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                }
            }
            else
            {
                uuid = uuid.replaceAll("-", "");
            }
        }

        return uuid;
    }
}
