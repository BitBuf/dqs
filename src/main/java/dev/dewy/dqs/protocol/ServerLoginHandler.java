package dev.dewy.dqs.protocol;

import dev.dewy.dqs.protocol.core.Session;

public interface ServerLoginHandler
{
    void loggedIn(Session session);
}
