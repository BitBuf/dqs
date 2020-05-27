package dev.dewy.dqs.test.networking;

import dev.dewy.dqs.networking.event.session.*;

public class ServerSessionListener extends SessionAdapter
{
    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        if (event.getPacket() instanceof PingPacket)
        {
            System.out.println("SERVER Received: " + event.<PingPacket>getPacket().getPingId());
            event.getSession().send(event.getPacket());
        }
    }

    @Override
    public void packetSent(PacketSentEvent event)
    {
        if (event.getPacket() instanceof PingPacket)
        {
            System.out.println("SERVER Sent: " + event.<PingPacket>getPacket().getPingId());
        }
    }

    @Override
    public void connected(ConnectedEvent event)
    {
        System.out.println("SERVER Connected");
    }

    @Override
    public void disconnecting(DisconnectingEvent event)
    {
        System.out.println("SERVER Disconnecting: " + event.getReason());
    }

    @Override
    public void disconnected(DisconnectedEvent event)
    {
        System.out.println("SERVER Disconnected: " + event.getReason());
    }
}
