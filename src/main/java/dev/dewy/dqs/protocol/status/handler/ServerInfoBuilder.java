package dev.dewy.dqs.protocol.status.handler;

import dev.dewy.dqs.protocol.status.ServerStatusInfo;
import dev.dewy.dqs.networking.Session;

public interface ServerInfoBuilder
{
    public ServerStatusInfo buildInfo(Session session);
}
