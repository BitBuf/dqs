package dev.dewy.dqs.server;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.networking.event.server.*;
import dev.dewy.dqs.protocol.DQSProtocol;
import dev.dewy.dqs.protocol.SubProtocol;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import static dev.dewy.dqs.utils.Constants.SERVER_LOG;

public class DQSServerListener implements ServerListener
{
    protected final DQS dqs;

    protected final Map<Session, DQSServerConnection> connections = Collections.synchronizedMap(new IdentityHashMap<>());

    //this isn't really needed, but it lets me print the correct address to the log
    //TODO: ip-ban specific clients?
    protected final Map<Session, SocketAddress> addresses = Collections.synchronizedMap(new IdentityHashMap<>());

    public DQSServerListener(DQS dqs)
    {
        this.dqs = dqs;
    }

    @Override
    public void serverBound(ServerBoundEvent event)
    {
        SERVER_LOG.success("Server started.");
    }

    @Override
    public void serverClosing(ServerClosingEvent event)
    {
        SERVER_LOG.info("Closing server...");
    }

    @Override
    public void serverClosed(ServerClosedEvent event)
    {
        SERVER_LOG.success("Server closed.");
    }

    @Override
    public void sessionAdded(SessionAddedEvent event)
    {
        //SERVER_LOG.info("session added");
        if (((DQSProtocol) event.getSession().getPacketProtocol()).getSubProtocol() != SubProtocol.STATUS)
        {
            DQSServerConnection connection = new DQSServerConnection(this.dqs, event.getSession());
            event.getSession().addListener(connection);
            this.addresses.put(event.getSession(), event.getSession().getRemoteAddress());
            this.connections.put(event.getSession(), connection);
        }
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event)
    {
        this.addresses.remove(event.getSession());
        DQSServerConnection connection = this.connections.remove(event.getSession());
        if (connection != null)
        {
            this.dqs.getCurrentPlayer().compareAndSet(connection, null);
        }
    }

    public DQS getDqs()
    {
        return this.dqs;
    }

    public Map<Session, DQSServerConnection> getConnections()
    {
        return this.connections;
    }

    public Map<Session, SocketAddress> getAddresses()
    {
        return this.addresses;
    }
}
