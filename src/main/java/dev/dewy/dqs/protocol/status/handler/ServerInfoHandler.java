package dev.dewy.dqs.protocol.status.handler;

import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.protocol.status.ServerStatusInfo;

public interface ServerInfoHandler
{
    void handle(Session session, ServerStatusInfo info);
}
