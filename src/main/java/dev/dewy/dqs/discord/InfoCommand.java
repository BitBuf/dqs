package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;

public class InfoCommand extends Command
{
    public InfoCommand()
    {
        this.name = "info";
        this.help = "Display your account's biometrics and statistics.";
        this.aliases = new String[] {"stats", "statistics", "information", "figures", "i", "biometrics"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
    }

    @Override
    protected void execute(CommandEvent event)
    {

    }
}
