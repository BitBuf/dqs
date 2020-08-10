package dev.dewy.dqs.protocol;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.networking.Client;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.networking.tcp.TcpSessionFactory;

public class DQSSessionFactory extends TcpSessionFactory
{
    protected final DQS dqs;

    public DQSSessionFactory(DQS dqs)
    {
        this.dqs = dqs;
    }

    @Override
    public Session createClientSession(Client client)
    {
        return new DQSClientSession(client.getHost(), client.getPort(), client.getPacketProtocol(), client, this.dqs);
    }

    public DQS getDqs()
    {
        return this.dqs;
    }
}
