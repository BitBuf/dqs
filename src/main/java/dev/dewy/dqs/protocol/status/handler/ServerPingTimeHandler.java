package dev.dewy.dqs.protocol.status.handler;

import dev.dewy.dqs.protocol.core.Session;

public interface ServerPingTimeHandler
{
    void handle(Session session, long pingTime);
}
