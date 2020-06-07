package dev.dewy.dqs.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.packet.ingame.client.ClientChatPacket;
import dev.dewy.dqs.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class WhisperCommand extends Command
{
    public WhisperCommand()
    {
        this.name = "whisper";
        this.help = "Send a message to public chat.";
        this.aliases = new String[] {"msg", "w", "pm"};
        this.guildOnly = false;
        this.cooldown = Constants.CONFIG.discord.cooldown;
        this.arguments = "<RECIPIENT> <MESSAGE>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            String[] args = event.getArgs().split("\\s+");
            StringBuilder message = new StringBuilder();

            for (int i = 1; i < args.length; i++)
            {
                message.append(" ").append(args[i]);
            }

            message.insert(0, "/msg " + args[0] + " ");

            if (message.length() >= 255)
            {
                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Invalid Command Arguments")
                        .setDescription("You can not send a whisper larger than 255 characters. Please try and shorten your message.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Constants.CONFIG.authentication.username)
                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                        .build());

                return;
            }

            DQS.getInstance().getClient().getSession().send(new ClientChatPacket(message.toString()));

            event.reply(new EmbedBuilder()
                    .setTitle("**DQS** - Whisper")
                    .setDescription("The following message has been sent to **" + args[0] + "**:\n\n`" + message.substring((5 + args[0].length()) + 2) + "`")
                    .setColor(new Color(10144497))
                    .setFooter("Focused on " + Constants.CONFIG.authentication.username, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Constants.CONFIG.authentication.uuid)).toString())
                    .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                    .build());
        }
        catch (Throwable t)
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
                            .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a whisper command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                            .setColor(new Color(15221016))
                            .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build()).queue()));
        }
    }
}
