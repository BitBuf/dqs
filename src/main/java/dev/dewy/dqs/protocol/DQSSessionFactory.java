package dev.dewy.dqs.protocol;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.protocol.core.Client;
import dev.dewy.dqs.protocol.core.Session;
import dev.dewy.dqs.protocol.core.tcp.TcpSessionFactory;

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
