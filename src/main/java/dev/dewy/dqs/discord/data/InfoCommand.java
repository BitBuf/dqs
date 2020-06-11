package dev.dewy.dqs.discord.data;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.text.WordUtils;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class InfoCommand extends Command
{
    public InfoCommand()
    {
        this.name = "info";
        this.help = "Display your account's biometrics and statistics.";
        this.aliases = new String[] {"stats", "statistics", "information", "figures", "i", "biometrics"};
        this.guildOnly = false;
}

    private static float roundToHalf(float f)
    {
        return Math.round(f * 2.0F) / 2.0F;
    }

    public static String getEmojiFromBoolean(boolean in)
    {
        if (in)
        {
            return ":white_check_mark:";
        } else
        {
            return ":no_entry:";
        }
    }

    private static String getDimensionFromCode(int in)
    {
        if (in == 0)
        {
            return "overworld";
        }

        if (in == 1)
        {
            return "nether";
        }

        if (in == -1)
        {
            return "the end";
        }

        return "null";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Constants.CONFIG.service.subscriberId) || event.getAuthor().getId().equals(Constants.CONFIG.service.operatorId)) && (event.getChannel().getId().equals(Constants.CONFIG.service.channelId) || !event.getMessage().getChannelType().isGuild()) && Constants.CONFIG.modules.focus.focused)
        {
            try
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Statistics & Biometrics")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                        .addField("**Biometrics**", "**Health:** " + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getHealth()) + " (" + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getHealth()) / 2 + " :heart:)" + "\n**Hunger:** " + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getFood()) + " (" + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getFood()) / 2 + " :poultry_leg:)" + "\n**Saturation:** " + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getFood()) + " (" + roundToHalf(Constants.CACHE.getPlayerCache().getThePlayer().getFood()) / 2 + " :sparkles:)", true)
                        .addField("**Server**", "**IP:** " + Constants.CONFIG.client.server.address + "\n**Port:** " + Constants.CONFIG.client.server.port + "\n**Max Players:** " + Constants.CACHE.getPlayerCache().getMaxPlayers(), true)
                        .addField("**World**", "**Gamemode:** " + WordUtils.capitalize(Constants.CACHE.getPlayerCache().getGameMode().toString().toLowerCase()) + "\n**Dimension:** " + WordUtils.capitalize(getDimensionFromCode(Constants.CACHE.getPlayerCache().getDimension())) + "\n**Difficulty:** " + WordUtils.capitalize(Constants.CACHE.getPlayerCache().getDifficulty().toString().toLowerCase()) + "\n**World Type:** " + WordUtils.capitalize(Constants.CACHE.getPlayerCache().getWorldType().toString().toLowerCase()), true)
                        .addField("**Discord**", "**User:** " + event.getJDA().getUserById(Constants.CONFIG.service.subscriberId).getName() + "\n**Operator:** " + event.getJDA().getUserById(Constants.CONFIG.service.operatorId).getName() + "\n**Cooldown:** " + Constants.CONFIG.service.cooldown, true)
                        .addField("**Identification**", "**Entity ID:** " + Constants.CACHE.getPlayerCache().getThePlayer().getEntityId(), true)
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of an info command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/QQHhpKT.png")
                                .build()).queue()));
            }
        }
    }
}
