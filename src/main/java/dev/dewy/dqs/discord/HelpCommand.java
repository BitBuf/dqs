package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class HelpCommand extends Command
{
    public HelpCommand()
    {
        this.name = "help";
        this.help = "DQS help directory.";
        this.aliases = new String[] {"h", "halp", "?"};
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
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Help")
                        .setDescription("Please visit [here](https://dewy.dev/dqs/features) for a commands / features list.\n\nIf you have any other queries, consult the [FAQ](https://dewy.dev/dqs/faq) before contacting Dewy.")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .build());
            } catch (MalformedURLException e)
            {
                Constants.DISCORD_LOG.error(e);

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Error")
                        .setDescription("An exception occurred whilst executing this command. Debug information has been sent to Dewy to be fixed in following updates. Sorry about any inconvenience!")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .build());

                Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Constants.CONFIG.discord.subscriberId)).getName() + ")")
                                .setDescription("A " + e.getClass().getSimpleName() + " was thrown during the execution of a help command.\n\n**Cause:**\n\n```" + e.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}

