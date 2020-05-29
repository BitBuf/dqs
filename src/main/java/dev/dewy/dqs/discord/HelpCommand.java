package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class HelpCommand extends Command
{
    public HelpCommand()
    {
        this.name = "help";
        this.help = "DQS help directory.";
        this.aliases = new String[] { "h", "halp", "?" };
        this.category = new Category("Information");
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Help")
                    .setDescription("Please visit [here](https://dewy.dev/dqs/features) for a commands / features list.\n\nIf you have any other queries, consult the [FAQ](https://dewy.dev/dqs/faq) before contacting Dewy.")
                    .setColor(new Color(10144497))
                    .setFooter("Signed in as " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                    .setAuthor("DQS 3.0.0", null, "https://i.imgur.com/xTd3Ri3.png")
                    .build());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }
}

