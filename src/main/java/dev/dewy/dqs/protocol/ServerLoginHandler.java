package dev.dewy.dqs.protocol;

import dev.dewy.dqs.networking.Session;

public interface ServerLoginHandler
{
    void loggedIn(Session session);
}
