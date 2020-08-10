package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.login.server.LoginSuccessPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class LoginSuccessHandler implements HandlerRegistry.IncomingHandler<LoginSuccessPacket, DQSClientSession>
{
    @Override
    public boolean apply(LoginSuccessPacket packet, DQSClientSession session)
    {
        CACHE.getProfileCache().setProfile(packet.getProfile());
        return false;
    }

    @Override
    public Class<LoginSuccessPacket> getPacketClass()
    {
        return LoginSuccessPacket.class;
    }
}
