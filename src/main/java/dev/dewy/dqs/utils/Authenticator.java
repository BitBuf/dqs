package dev.dewy.dqs.utils;

import dev.dewy.dqs.utils.exceptions.request.RequestException;
import dev.dewy.dqs.protocol.DQSProtocol;
import dev.dewy.dqs.services.AuthenticationService;
import net.daporkchop.lib.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.net.Proxy;
import java.util.Objects;
import java.util.UUID;

import static dev.dewy.dqs.utils.Constants.*;

public class Authenticator
{
    protected final AuthenticationService auth;

    public Authenticator()
    {
        if (CONFIG.authentication.doAuthentication)
        {
            if (CONFIG.authentication.tokenUuid.equals("default"))
            {
                CONFIG.authentication.tokenUuid = UUID.randomUUID().toString();
            }

            saveConfig();

            if (CONFIG.authentication.tonken.equals("default"))
            {
                DEFAULT_LOG.log(LogLevel.WARN, "PASS");

                this.auth = new AuthenticationService(CONFIG.authentication.tokenUuid, Proxy.NO_PROXY);
                this.auth.setUsername(CONFIG.authentication.email);
                this.auth.setPassword(CONFIG.authentication.password);
            } else
            {
                DEFAULT_LOG.log(LogLevel.WARN, "TOKEN");

                this.auth = new AuthenticationService(CONFIG.authentication.tokenUuid, Proxy.NO_PROXY);
                this.auth.setUsername(CONFIG.authentication.email);
                this.auth.setAccessToken(CONFIG.authentication.tonken);
            }
        } else
        {
            this.auth = null;
        }
    }

    public DQSProtocol handleRelog()
    {
        if (this.auth == null)
        {
            return new DQSProtocol(CONFIG.authentication.username);
        } else
        {
            try
            {
                this.auth.login();

                CONFIG.authentication.tonken = auth.getAccessToken();

                saveConfig();

                return new DQSProtocol(
                        this.auth.getSelectedProfile(),
                        this.auth.getClientToken(),
                        this.auth.getAccessToken()
                );

            } catch (RequestException e)
            {
                if (!CONFIG.authentication.hasAuthenticated)
                {
                    try
                    {
                        Thread.sleep(3000L);

                        Objects.requireNonNull(DISCORD.getUserById(CONFIG.service.subscriberId)).openPrivateChannel().queue((privateChannel ->
                                privateChannel.sendMessage(new EmbedBuilder()
                                        .setAuthor("DQS " + Constants.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                        .setTitle("**DQS** - Failed To Authenticate")
                                        .setDescription("You've failed to authenticate with the target server `" + CONFIG.client.server.address + "`. Try using `&signin` again.")
                                        .setColor(new Color(10144497))
                                        .setFooter("Notification intended for " + Constants.CONFIG.authentication.username)
                                        .build()).queue()));
                    } catch (Throwable t)
                    {
                        DEFAULT_LOG.alert(t);
                    }
                }

                throw new RuntimeException(String.format(
                        "Unable to log in using credentials %s:%s (%s)",
                        CONFIG.authentication.email,
                        CONFIG.authentication.password,
                        CONFIG.authentication.username), e);
            }
        }
    }

    public AuthenticationService getAuth()
    {
        return this.auth;
    }
}
