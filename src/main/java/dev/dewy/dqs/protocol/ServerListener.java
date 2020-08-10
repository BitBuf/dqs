package dev.dewy.dqs.protocol;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.dewy.dqs.crypto.CryptUtil;
import dev.dewy.dqs.exceptions.request.RequestException;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.networking.event.session.ConnectedEvent;
import dev.dewy.dqs.networking.event.session.DisconnectingEvent;
import dev.dewy.dqs.networking.event.session.PacketReceivedEvent;
import dev.dewy.dqs.networking.event.session.SessionAdapter;
import dev.dewy.dqs.packet.client.HandshakePacket;
import dev.dewy.dqs.packet.ingame.client.ClientKeepAlivePacket;
import dev.dewy.dqs.packet.ingame.server.ServerDisconnectPacket;
import dev.dewy.dqs.packet.ingame.server.ServerKeepAlivePacket;
import dev.dewy.dqs.packet.login.client.EncryptionResponsePacket;
import dev.dewy.dqs.packet.login.client.LoginStartPacket;
import dev.dewy.dqs.packet.login.server.EncryptionRequestPacket;
import dev.dewy.dqs.packet.login.server.LoginDisconnectPacket;
import dev.dewy.dqs.packet.login.server.LoginSetCompressionPacket;
import dev.dewy.dqs.packet.login.server.LoginSuccessPacket;
import dev.dewy.dqs.packet.status.client.StatusPingPacket;
import dev.dewy.dqs.packet.status.client.StatusQueryPacket;
import dev.dewy.dqs.packet.status.server.StatusPongPacket;
import dev.dewy.dqs.packet.status.server.StatusResponsePacket;
import dev.dewy.dqs.profiles.GameProfile;
import dev.dewy.dqs.protocol.status.PlayerInfo;
import dev.dewy.dqs.protocol.status.ServerStatusInfo;
import dev.dewy.dqs.protocol.status.VersionInfo;
import dev.dewy.dqs.protocol.status.handler.ServerInfoBuilder;
import dev.dewy.dqs.services.SessionService;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.Proxy;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class ServerListener extends SessionAdapter
{
    private static final KeyPair KEY_PAIR = CryptUtil.generateKeyPair();

    private final byte[] verifyToken = new byte[4];
    private final String serverId = "";
    private String username = "";

    private long lastPingTime = 0;
    private int lastPingId = 0;

    public ServerListener()
    {
        new Random().nextBytes(this.verifyToken);
    }

    @Override
    public void connected(ConnectedEvent event)
    {
        event.getSession().setFlag(MinecraftConstants.PING_KEY, 0);
    }

    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        DQSProtocol protocol = (DQSProtocol) event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.HANDSHAKE)
        {
            if (event.getPacket() instanceof HandshakePacket)
            {
                HandshakePacket packet = event.getPacket();
                switch (packet.getIntent())
                {
                    case STATUS:
                        protocol.setSubProtocol(SubProtocol.STATUS, false, event.getSession());
                        break;
                    case LOGIN:
                        protocol.setSubProtocol(SubProtocol.LOGIN, false, event.getSession());
                        if (packet.getProtocolVersion() > MinecraftConstants.PROTOCOL_VERSION)
                        {
                            event.getSession().disconnect("Outdated server! I'm still on " + MinecraftConstants.GAME_VERSION + ".");
                        } else if (packet.getProtocolVersion() < MinecraftConstants.PROTOCOL_VERSION)
                        {
                            event.getSession().disconnect("Outdated client! Please use " + MinecraftConstants.GAME_VERSION + ".");
                        }

                        break;
                    default:
                        throw new UnsupportedOperationException("Invalid client intent: " + packet.getIntent());
                }
            }
        }

        if (protocol.getSubProtocol() == SubProtocol.LOGIN)
        {
            if (event.getPacket() instanceof LoginStartPacket)
            {
                this.username = event.<LoginStartPacket>getPacket().getUsername();

                boolean verify = event.getSession().hasFlag(MinecraftConstants.VERIFY_USERS_KEY) ? event.getSession().<Boolean>getFlag(MinecraftConstants.VERIFY_USERS_KEY) : true;
                if (verify)
                {
                    event.getSession().send(new EncryptionRequestPacket(this.serverId, KEY_PAIR.getPublic(), this.verifyToken));
                } else
                {
                    new Thread(new UserAuthTask(event.getSession(), null)).start();
                }
            } else if (event.getPacket() instanceof EncryptionResponsePacket)
            {
                EncryptionResponsePacket packet = event.getPacket();
                PrivateKey privateKey = KEY_PAIR.getPrivate();
                if (!Arrays.equals(this.verifyToken, packet.getVerifyToken(privateKey)))
                {
                    event.getSession().disconnect("Invalid nonce!");
                    return;
                }

                SecretKey key = packet.getSecretKey(privateKey);
                protocol.enableEncryption(key);
                new Thread(new UserAuthTask(event.getSession(), key)).start();
            }
        }

        if (protocol.getSubProtocol() == SubProtocol.STATUS)
        {
            if (event.getPacket() instanceof StatusQueryPacket)
            {
                ServerInfoBuilder builder = event.getSession().getFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY);
                if (builder == null)
                {
                    builder = new ServerInfoBuilder()
                    {
                        @Override
                        public ServerStatusInfo buildInfo(Session session)
                        {
                            return new ServerStatusInfo(VersionInfo.CURRENT, new PlayerInfo(0, 20, new GameProfile[] {}), "A Minecraft Server", null, true);
                        }
                    };
                }

                ServerStatusInfo info = builder.buildInfo(event.getSession());
                event.getSession().send(new StatusResponsePacket(info));
            } else if (event.getPacket() instanceof StatusPingPacket)
            {
                event.getSession().send(new StatusPongPacket(event.<StatusPingPacket>getPacket().getPingTime()));
            }
        }

        if (protocol.getSubProtocol() == SubProtocol.GAME)
        {
            if (event.getPacket() instanceof ClientKeepAlivePacket)
            {
                ClientKeepAlivePacket packet = event.getPacket();
                if (packet.getPingId() == this.lastPingId)
                {
                    long time = System.currentTimeMillis() - this.lastPingTime;
                    event.getSession().setFlag(MinecraftConstants.PING_KEY, time);
                }
            }
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event)
    {
        DQSProtocol protocol = (DQSProtocol) event.getSession().getPacketProtocol();
        boolean escape = false;
        if (event.getReason() != null)
        {
            try
            {
                new JsonParser().parse(event.getReason());
                escape = false;
            } catch (JsonSyntaxException ignored)
            {
                escape = true;
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.LOGIN)
        {
            event.getSession().send(new LoginDisconnectPacket(event.getReason(), escape));
        } else if (protocol.getSubProtocol() == SubProtocol.GAME)
        {
            event.getSession().send(new ServerDisconnectPacket(event.getReason(), escape));
        }
    }

    private class UserAuthTask implements Runnable
    {
        private final Session session;
        private final SecretKey key;

        public UserAuthTask(Session session, SecretKey key)
        {
            this.key = key;
            this.session = session;
        }

        @Override
        public void run()
        {
            boolean verify = this.session.hasFlag(MinecraftConstants.VERIFY_USERS_KEY) ? this.session.<Boolean>getFlag(MinecraftConstants.VERIFY_USERS_KEY) : true;

            GameProfile profile = null;
            if (verify && this.key != null)
            {
                Proxy proxy = this.session.getFlag(MinecraftConstants.AUTH_PROXY_KEY);
                if (proxy == null)
                {
                    proxy = Proxy.NO_PROXY;
                }

                try
                {
                    profile = new SessionService(proxy).getProfileByServer(username, new BigInteger(CryptUtil.getServerIdHash(serverId, KEY_PAIR.getPublic(), this.key)).toString(16));
                } catch (RequestException e)
                {
                    this.session.disconnect("Failed to make session service request.", e);
                    return;
                }

                if (profile == null)
                {
                    this.session.disconnect("Failed to verify username.");
                }
            } else
            {
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()), username);
            }

            int threshold;
            if (this.session.hasFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD))
            {
                threshold = this.session.getFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD);
            } else
            {
                threshold = 256;
            }

            this.session.send(new LoginSetCompressionPacket(threshold));
            this.session.setCompressionThreshold(threshold);
            this.session.send(new LoginSuccessPacket(profile));
            this.session.setFlag(MinecraftConstants.PROFILE_KEY, profile);
            ((DQSProtocol) this.session.getPacketProtocol()).setSubProtocol(SubProtocol.GAME, false, this.session);
            ServerLoginHandler handler = this.session.getFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY);
            if (handler != null)
            {
                handler.loggedIn(this.session);
            }

            new Thread(new KeepAliveTask(this.session)).start();
        }
    }

    private class KeepAliveTask implements Runnable
    {
        private final Session session;

        public KeepAliveTask(Session session)
        {
            this.session = session;
        }

        @Override
        public void run()
        {
            while (this.session.isConnected())
            {
                lastPingTime = System.currentTimeMillis();
                lastPingId = (int) lastPingTime;
                this.session.send(new ServerKeepAlivePacket(lastPingId));

                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    }
}
