package dev.dewy.dqs.protocol.status.handler;

import dev.dewy.dqs.networking.Session;

public interface ServerPingTimeHandler
{
    void handle(Session session, long pingTime);
}
