package dev.dewy.dqs.server.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.login.client.LoginStartPacket;
import dev.dewy.dqs.server.DQSServerConnection;

import static dev.dewy.dqs.utils.Constants.CONFIG;
import static dev.dewy.dqs.utils.Constants.SERVER_LOG;

public class LoginStartHandler implements HandlerRegistry.IncomingHandler<LoginStartPacket, DQSServerConnection>
{
    @Override
    public boolean apply(LoginStartPacket packet, DQSServerConnection session)
    {
        //SERVER_LOG.info("login start");
        if (CONFIG.modules.whitelist.enabled && !CONFIG.modules.whitelist.whitelist.contains(packet.getUsername()))
        {
            SERVER_LOG.warn("User %s [%s] tried to connect!", packet.getUsername(), session.getRemoteAddress());
            session.disconnect(CONFIG.modules.whitelist.kickmsg);
            return false;
        }

        if (DQS.isRecon)
        {
            session.disconnect("Currently relogging. Please wait a little bit.");
        }

        return false;
    }

    @Override
    public Class<LoginStartPacket> getPacketClass()
    {
        return LoginStartPacket.class;
    }
}
