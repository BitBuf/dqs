package dev.dewy.dqs.client;

import dev.dewy.dqs.networking.Client;
import dev.dewy.dqs.packet.PacketProtocol;
import dev.dewy.dqs.networking.tcp.TcpClientSession;
import net.daporkchop.lib.unsafe.PUnsafe;
import dev.dewy.dqs.DQS;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class DQSClientSession extends TcpClientSession
{
    protected final CompletableFuture<String> disconnectFuture = new CompletableFuture<>();
    protected final DQS dqs;

    public DQSClientSession(String host, int port, PacketProtocol protocol, Client client, DQS dqs)
    {
        super(host, port, protocol, client, null);
        this.dqs = dqs;
        this.addListener(new ClientListener(this.dqs, this));
    }

    public String getDisconnectReason()
    {
        try
        {
            return this.disconnectFuture.get();
        } catch (Exception e)
        {
            PUnsafe.throwException(e);
            return null;
        }
    }

    @Override
    public void disconnect(String reason, Throwable cause, boolean wait)
    {
        super.disconnect(reason, cause, wait);
        if (cause == null)
        {
            this.disconnectFuture.complete(reason);
        } else if (cause instanceof IOException)
        {
            this.disconnectFuture.complete(String.format("IOException: %s", cause.getMessage()));
        } else
        {
            CLIENT_LOG.alert(cause);
            this.disconnectFuture.completeExceptionally(cause);
        }
    }

    public DQS getDqs()
    {
        return this.dqs;
    }

    private CompletableFuture<String> getDisconnectFuture()
    {
        return this.disconnectFuture;
    }
}
