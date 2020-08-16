package dev.dewy.dqs.test.auth;

import dev.dewy.dqs.utils.exceptions.request.RequestException;
import dev.dewy.dqs.protocol.profiles.GameProfile;
import dev.dewy.dqs.services.AuthenticationService;
import dev.dewy.dqs.services.ProfileService;
import dev.dewy.dqs.services.SessionService;

import java.net.Proxy;
import java.util.UUID;

public class MinecraftAuthTest
{
    private static final String USERNAME = "chunky";
    private static final String EMAIL = "cg@coconut.gun";
    private static final String PASSWORD = "beanos640";
    private static final String ACCESS_TOKEN = null;

    private static final Proxy PROXY = Proxy.NO_PROXY;

    public static void main(String[] args)
    {
        profileLookup();
        auth();
    }

    private static void profileLookup()
    {
        ProfileService repository = new ProfileService(PROXY);
        repository.findProfilesByName(new String[] {USERNAME}, new ProfileService.ProfileLookupCallback()
        {
            @Override
            public void onProfileLookupSucceeded(GameProfile profile)
            {
                System.out.println("Found profile: " + profile);
            }

            @Override
            public void onProfileLookupFailed(GameProfile profile, Exception e)
            {
                System.out.println("Lookup for profile " + profile.getName() + " failed!");
                e.printStackTrace();
            }
        });
    }

    private static void auth()
    {
        String clientToken = UUID.randomUUID().toString();
        AuthenticationService auth = new AuthenticationService(clientToken, PROXY);
        auth.setUsername(EMAIL);
        if (ACCESS_TOKEN != null)
        {
            auth.setAccessToken(ACCESS_TOKEN);
        } else
        {
            auth.setPassword(PASSWORD);
        }

        try
        {
            auth.login();
        } catch (RequestException e)
        {
            System.err.println("Failed to log in!");
            e.printStackTrace();
            return;
        }

        SessionService service = new SessionService();
        for (GameProfile profile : auth.getAvailableProfiles())
        {
            try
            {
                service.fillProfileProperties(profile);
                service.fillProfileTextures(profile, false);
            } catch (Exception e)
            {
                System.err.println("Failed to get properties and textures of profile " + profile + ".");
                e.printStackTrace();
            }

            System.out.println("Profile: " + profile);
        }
    }
}
