package dev.dewy.dqs.protocol.status.handler;

import dev.dewy.dqs.protocol.core.Session;
import dev.dewy.dqs.protocol.status.ServerStatusInfo;

public interface ServerInfoBuilder
{
    ServerStatusInfo buildInfo(Session session);
}
