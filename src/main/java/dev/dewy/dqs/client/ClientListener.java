package dev.dewy.dqs.client;

import dev.dewy.dqs.protocol.MinecraftProtocol;
import dev.dewy.dqs.protocol.SubProtocol;
import dev.dewy.dqs.networking.event.session.*;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.DQS;
import dev.dewy.dqs.server.DQSServerConnection;

import static dev.dewy.dqs.utils.Constants.*;

public class ClientListener implements SessionListener
{
    protected final DQS dqs;

    protected final DQSClientSession session;

    public ClientListener(DQS dqs, DQSClientSession session)
    {
        this.dqs = dqs;
        this.session = session;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        try
        {
            if (CLIENT_HANDLERS.handleInbound(event.getPacket(), this.session))
            {
                DQSServerConnection connection = this.dqs.getCurrentPlayer().get();
                if (connection != null && ((MinecraftProtocol) connection.getPacketProtocol()).getSubProtocol() == SubProtocol.GAME)
                {
                    connection.send(event.getPacket());
                }
            }
        } catch (RuntimeException e)
        {
            CLIENT_LOG.alert(e);
            throw e;
        } catch (Exception e)
        {
            CLIENT_LOG.alert(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void packetSending(PacketSendingEvent event)
    {
        try
        {
            Packet p1 = event.getPacket();
            Packet p2 = CLIENT_HANDLERS.handleOutgoing(p1, this.session);
            if (p2 == null)
            {
                event.setCancelled(true);
            } else if (p1 != p2)
            {
                event.setPacket(p2);
            }
        } catch (Exception e)
        {
            CLIENT_LOG.alert(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void packetSent(PacketSentEvent event)
    {
        try
        {
            CLIENT_HANDLERS.handlePostOutgoing(event.getPacket(), this.session);
        } catch (Exception e)
        {
            CLIENT_LOG.alert(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connected(ConnectedEvent event)
    {
        WEBSOCKET_SERVER.fireReset();
        CLIENT_LOG.success("Connected to %s!", event.getSession().getRemoteAddress());
    }

    @Override
    public void disconnecting(DisconnectingEvent event)
    {
        WEBSOCKET_SERVER.fireReset();
        CLIENT_LOG.info("Disconnecting from server...")
                .trace("Disconnect reason: %s", event.getReason());

        DQSServerConnection connection = this.dqs.getCurrentPlayer().get();
        if (connection != null)
        {
            connection.disconnect(event.getReason());
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event)
    {
        WEBSOCKET_SERVER.fireReset();
        CLIENT_LOG.info("Disconnected.");
    }

    public DQS getDqs()
    {
        return this.dqs;
    }

    public DQSClientSession getSession()
    {
        return this.session;
    }
}
