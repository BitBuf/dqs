package dev.dewy.dqs.server.handler.incoming;

import dev.dewy.dqs.packet.login.client.LoginStartPacket;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.CONFIG;
import static dev.dewy.dqs.utils.Constants.SERVER_LOG;

public class LoginStartHandler implements HandlerRegistry.IncomingHandler<LoginStartPacket, DQSServerConnection>
{
    @Override
    public boolean apply(LoginStartPacket packet, DQSServerConnection session)
    {
        //SERVER_LOG.info("login start");
        if (CONFIG.server.extra.whitelist.enable && !CONFIG.server.extra.whitelist.allowedUsers.contains(packet.getUsername()))
        {
            SERVER_LOG.warn("User %s [%s] tried to connect!", packet.getUsername(), session.getRemoteAddress());
            session.disconnect(CONFIG.server.extra.whitelist.kickmsg);
            return false;
        }
        if (!DQS.getInstance().isConnected())
        {
            session.disconnect("Not connected to server!");
        }
        return false;
    }

    @Override
    public Class<LoginStartPacket> getPacketClass()
    {
        return LoginStartPacket.class;
    }
}
