package dev.dewy.dqs.utils;

import dev.dewy.dqs.exceptions.request.RequestException;
import dev.dewy.dqs.services.AuthenticationService;
import dev.dewy.dqs.protocol.MinecraftProtocol;

import java.net.Proxy;
import java.util.UUID;

import static dev.dewy.dqs.utils.Constants.CONFIG;

public class Authenticator
{
    protected final AuthenticationService auth;

    public Authenticator()
    {
        if (CONFIG.authentication.doAuthentication)
        {
            this.auth = new AuthenticationService(UUID.randomUUID().toString(), Proxy.NO_PROXY);
            this.auth.setUsername(CONFIG.authentication.email);
            this.auth.setPassword(CONFIG.authentication.password);
        } else
        {
            this.auth = null;
        }
    }

    public MinecraftProtocol handleRelog()
    {
        if (this.auth == null)
        {
            return new MinecraftProtocol(CONFIG.authentication.username);
        } else
        {
            try
            {
                this.auth.login();
                return new MinecraftProtocol(
                        this.auth.getSelectedProfile(),
                        this.auth.getClientToken(),
                        this.auth.getAccessToken()
                );
            } catch (RequestException e)
            {
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
