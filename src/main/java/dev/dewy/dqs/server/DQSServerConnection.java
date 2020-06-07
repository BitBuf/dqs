package dev.dewy.dqs.server;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.networking.event.session.*;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.PacketProtocol;
import dev.dewy.dqs.protocol.DQSProtocol;
import dev.dewy.dqs.protocol.SubProtocol;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.Map;

import static dev.dewy.dqs.utils.Constants.SERVER_HANDLERS;
import static dev.dewy.dqs.utils.Constants.SERVER_LOG;

public class DQSServerConnection implements Session, SessionListener
{
    protected final DQS dqs;

    protected final Session session;

    protected long lastPacket = System.currentTimeMillis();

    protected boolean isPlayer = false;
    protected boolean isLoggedIn = false;

    public DQSServerConnection(DQS dqs, Session session)
    {
        this.dqs = dqs;
        this.session = session;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        this.lastPacket = System.currentTimeMillis();
        if (SERVER_HANDLERS.handleInbound(event.getPacket(), this) && ((DQSProtocol) this.session.getPacketProtocol()).getSubProtocol() == SubProtocol.GAME && this.isLoggedIn)
        {
            this.dqs.getClient().getSession().send(event.getPacket()); //TODO: handle multi-client correctly (i.e. only allow one client to send packets at a time)
        }
    }

    @Override
    public void packetSending(PacketSendingEvent event)
    {
        Packet p1 = event.getPacket();
        Packet p2 = SERVER_HANDLERS.handleOutgoing(p1, this);
        if (p2 == null)
        {
            event.setCancelled(true);
        } else if (p1 != p2)
        {
            event.setPacket(p2);
        }
    }

    @Override
    public void packetSent(PacketSentEvent event)
    {
        SERVER_HANDLERS.handlePostOutgoing(event.getPacket(), this);
    }

    @Override
    public void connected(ConnectedEvent event)
    {
    }

    @Override
    public void disconnecting(DisconnectingEvent event)
    {
    }

    @Override
    public void disconnected(DisconnectedEvent event)
    {
        if (event.getCause() != null && !((event.getCause() instanceof IOException || event.getCause() instanceof ClosedChannelException) && !this.isPlayer))
        {
            SERVER_LOG.alert(String.format("Player disconnected: %s", event.getSession().getRemoteAddress()), event.getCause());

            DQS.getInstance().connectedToProxy = false;
        } else if (this.isPlayer)
        {
            DQS.getInstance().connectedToProxy = false;
            SERVER_LOG.info("Player disconnected: %s", event.getSession().getRemoteAddress());
        }
    }

    public void send(Packet packet)
    {
        this.session.send(packet);
    }

    //
    //
    //
    // SESSION METHOD IMPLEMENTATIONS
    //
    //
    //

    @Override
    public void connect()
    {
        this.session.connect();
    }

    @Override
    public void connect(boolean wait)
    {
        this.session.connect(wait);
    }

    @Override
    public String getHost()
    {
        return this.session.getHost();
    }

    @Override
    public int getPort()
    {
        return this.session.getPort();
    }

    @Override
    public SocketAddress getLocalAddress()
    {
        return this.session.getLocalAddress();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return this.session.getRemoteAddress();
    }

    @Override
    public PacketProtocol getPacketProtocol()
    {
        return this.session.getPacketProtocol();
    }

    @Override
    public Map<String, Object> getFlags()
    {
        return this.session.getFlags();
    }

    @Override
    public boolean hasFlag(String key)
    {
        return this.session.hasFlag(key);
    }

    @Override
    public <T> T getFlag(String key)
    {
        return this.session.getFlag(key);
    }

    @Override
    public void setFlag(String key, Object value)
    {
        this.session.setFlag(key, value);
    }

    @Override
    public List<SessionListener> getListeners()
    {
        return this.session.getListeners();
    }

    @Override
    public void addListener(SessionListener listener)
    {
        this.session.addListener(listener);
    }

    @Override
    public void removeListener(SessionListener listener)
    {
        this.session.removeListener(listener);
    }

    @Override
    public void callEvent(SessionEvent event)
    {
        this.session.callEvent(event);
    }

    @Override
    public int getCompressionThreshold()
    {
        return this.session.getCompressionThreshold();
    }

    @Override
    public void setCompressionThreshold(int threshold)
    {
        this.session.setCompressionThreshold(threshold);
    }

    @Override
    public int getConnectTimeout()
    {
        return this.session.getConnectTimeout();
    }

    @Override
    public void setConnectTimeout(int timeout)
    {
        this.session.setConnectTimeout(timeout);
    }

    @Override
    public int getReadTimeout()
    {
        return this.session.getReadTimeout();
    }

    @Override
    public void setReadTimeout(int timeout)
    {
        this.session.setReadTimeout(timeout);
    }

    @Override
    public int getWriteTimeout()
    {
        return this.session.getWriteTimeout();
    }

    @Override
    public void setWriteTimeout(int timeout)
    {
        this.session.setWriteTimeout(timeout);
    }

    @Override
    public boolean isConnected()
    {
        return this.session.isConnected();
    }

    @Override
    public void disconnect(String reason)
    {
        this.session.disconnect(reason);
    }

    @Override
    public void disconnect(String reason, boolean wait)
    {
        this.session.disconnect(reason, wait);
    }

    @Override
    public void disconnect(String reason, Throwable cause)
    {
        this.session.disconnect(reason, cause);
    }

    @Override
    public void disconnect(String reason, Throwable cause, boolean wait)
    {
        this.session.disconnect(reason, cause, wait);
    }

    public DQS getDqs()
    {
        return this.dqs;
    }

    public Session getSession()
    {
        return this.session;
    }

    public long getLastPacket()
    {
        return this.lastPacket;
    }

    public void setLastPacket(long lastPacket)
    {
        this.lastPacket = lastPacket;
    }

    public boolean isPlayer()
    {
        return this.isPlayer;
    }

    public void setPlayer(boolean isPlayer)
    {
        this.isPlayer = isPlayer;
    }

    public boolean isLoggedIn()
    {
        return this.isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn)
    {
        this.isLoggedIn = isLoggedIn;
    }
}
