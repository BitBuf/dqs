package dev.dewy.dqs.protocol;

import dev.dewy.dqs.networking.Session;

public interface ServerLoginHandler
{
    public void loggedIn(Session session);
}
