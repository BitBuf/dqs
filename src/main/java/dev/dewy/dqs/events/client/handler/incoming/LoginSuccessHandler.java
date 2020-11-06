package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.login.server.LoginSuccessPacket;

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
