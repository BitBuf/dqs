package dev.dewy.dqs.events.server.handler.outgoing;

import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.login.server.LoginSuccessPacket;
import dev.dewy.dqs.events.server.DQSServerConnection;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.SERVER_LOG;

public class LoginSuccessOutgoingHandler implements HandlerRegistry.OutgoingHandler<LoginSuccessPacket, DQSServerConnection>
{
    @Override
    public LoginSuccessPacket apply(LoginSuccessPacket packet, DQSServerConnection session)
    {
        SERVER_LOG.debug("User UUID: %s\nBot UUID: %s", packet.getProfile().getId().toString(), CACHE.getProfileCache().getProfile().getId().toString());
        return new LoginSuccessPacket(CACHE.getProfileCache().getProfile());
    }

    @Override
    public Class<LoginSuccessPacket> getPacketClass()
    {
        return LoginSuccessPacket.class;
    }
}
