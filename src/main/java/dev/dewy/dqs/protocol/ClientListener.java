package dev.dewy.dqs.protocol;

import dev.dewy.dqs.utils.crypto.CryptUtil;
import dev.dewy.dqs.utils.exceptions.request.InvalidCredentialsException;
import dev.dewy.dqs.utils.exceptions.request.RequestException;
import dev.dewy.dqs.utils.exceptions.request.ServiceUnavailableException;
import dev.dewy.dqs.protocol.core.event.session.ConnectedEvent;
import dev.dewy.dqs.protocol.core.event.session.PacketReceivedEvent;
import dev.dewy.dqs.protocol.core.event.session.SessionAdapter;
import dev.dewy.dqs.protocol.packet.client.HandshakePacket;
import dev.dewy.dqs.protocol.packet.ingame.client.ClientKeepAlivePacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerDisconnectPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerKeepAlivePacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerSetCompressionPacket;
import dev.dewy.dqs.protocol.packet.login.client.EncryptionResponsePacket;
import dev.dewy.dqs.protocol.packet.login.client.LoginStartPacket;
import dev.dewy.dqs.protocol.packet.login.server.EncryptionRequestPacket;
import dev.dewy.dqs.protocol.packet.login.server.LoginDisconnectPacket;
import dev.dewy.dqs.protocol.packet.login.server.LoginSetCompressionPacket;
import dev.dewy.dqs.protocol.packet.login.server.LoginSuccessPacket;
import dev.dewy.dqs.protocol.packet.status.client.StatusPingPacket;
import dev.dewy.dqs.protocol.packet.status.client.StatusQueryPacket;
import dev.dewy.dqs.protocol.packet.status.server.StatusPongPacket;
import dev.dewy.dqs.protocol.packet.status.server.StatusResponsePacket;
import dev.dewy.dqs.protocol.profiles.GameProfile;
import dev.dewy.dqs.protocol.handshake.HandshakeIntent;
import dev.dewy.dqs.protocol.status.ServerStatusInfo;
import dev.dewy.dqs.protocol.status.handler.ServerInfoHandler;
import dev.dewy.dqs.protocol.status.handler.ServerPingTimeHandler;
import dev.dewy.dqs.services.SessionService;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.Proxy;

public class ClientListener extends SessionAdapter
{
    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        DQSProtocol protocol = (DQSProtocol) event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN)
        {
            if (event.getPacket() instanceof EncryptionRequestPacket)
            {
                EncryptionRequestPacket packet = event.getPacket();
                SecretKey key = CryptUtil.generateSharedKey();

                Proxy proxy = event.getSession().getFlag(MinecraftConstants.AUTH_PROXY_KEY);
                if (proxy == null)
                {
                    proxy = Proxy.NO_PROXY;
                }

                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                String serverHash = new BigInteger(CryptUtil.getServerIdHash(packet.getServerId(), packet.getPublicKey(), key)).toString(16);
                String accessToken = event.getSession().getFlag(MinecraftConstants.ACCESS_TOKEN_KEY);
                try
                {
                    new SessionService(proxy).joinServer(profile, accessToken, serverHash);
                } catch (ServiceUnavailableException e)
                {
                    event.getSession().disconnect("Login failed: Authentication service unavailable.", e);
                    return;
                } catch (InvalidCredentialsException e)
                {
                    event.getSession().disconnect("Login failed: Invalid login session.", e);
                    return;
                } catch (RequestException e)
                {
                    event.getSession().disconnect("Login failed: Authentication error: " + e.getMessage(), e);
                    return;
                }

                event.getSession().send(new EncryptionResponsePacket(key, packet.getPublicKey(), packet.getVerifyToken()));
                protocol.enableEncryption(key);
            } else if (event.getPacket() instanceof LoginSuccessPacket)
            {
                LoginSuccessPacket packet = event.getPacket();
                event.getSession().setFlag(MinecraftConstants.PROFILE_KEY, packet.getProfile());
                protocol.setSubProtocol(SubProtocol.GAME, true, event.getSession());
            } else if (event.getPacket() instanceof LoginDisconnectPacket)
            {
                LoginDisconnectPacket packet = event.getPacket();
                event.getSession().disconnect(packet.getReason());
            } else if (event.getPacket() instanceof LoginSetCompressionPacket)
            {
                event.getSession().setCompressionThreshold(event.<LoginSetCompressionPacket>getPacket().getThreshold());
            }
        } else if (protocol.getSubProtocol() == SubProtocol.STATUS)
        {
            if (event.getPacket() instanceof StatusResponsePacket)
            {
                ServerStatusInfo info = event.<StatusResponsePacket>getPacket().getInfo();
                ServerInfoHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null)
                {
                    handler.handle(event.getSession(), info);
                }

                event.getSession().send(new StatusPingPacket(System.currentTimeMillis()));
            } else if (event.getPacket() instanceof StatusPongPacket)
            {
                long time = System.currentTimeMillis() - event.<StatusPongPacket>getPacket().getPingTime();
                ServerPingTimeHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null)
                {
                    handler.handle(event.getSession(), time);
                }

                event.getSession().disconnect("Finished");
            }
        } else if (protocol.getSubProtocol() == SubProtocol.GAME)
        {
            if (event.getPacket() instanceof ServerKeepAlivePacket)
            {
                event.getSession().send(new ClientKeepAlivePacket(event.<ServerKeepAlivePacket>getPacket().getPingId()));
            } else if (event.getPacket() instanceof ServerDisconnectPacket)
            {
                event.getSession().disconnect(event.<ServerDisconnectPacket>getPacket().getReason());
            } else if (event.getPacket() instanceof ServerSetCompressionPacket)
            {
                event.getSession().setCompressionThreshold(event.<ServerSetCompressionPacket>getPacket().getThreshold());
            }
        }
    }

    @Override
    public void connected(ConnectedEvent event)
    {
        DQSProtocol protocol = (DQSProtocol) event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN)
        {
            GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
            protocol.setSubProtocol(SubProtocol.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(MinecraftConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
            protocol.setSubProtocol(SubProtocol.LOGIN, true, event.getSession());
            event.getSession().send(new LoginStartPacket(profile != null ? profile.getName() : ""));
        } else if (protocol.getSubProtocol() == SubProtocol.STATUS)
        {
            protocol.setSubProtocol(SubProtocol.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(MinecraftConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
            protocol.setSubProtocol(SubProtocol.STATUS, true, event.getSession());
            event.getSession().send(new StatusQueryPacket());
        }
    }
}
