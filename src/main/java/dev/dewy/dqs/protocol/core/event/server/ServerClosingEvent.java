package dev.dewy.dqs.protocol.core.event.server;

import dev.dewy.dqs.protocol.core.Server;

/**
 * Called when the server is about to close.
 */
public class ServerClosingEvent implements ServerEvent
{
    private final Server server;

    /**
     * Creates a new ServerClosingEvent instance.
     *
     * @param server Server being closed.
     */
    public ServerClosingEvent(Server server)
    {
        this.server = server;
    }

    /**
     * Gets the server involved in this event.
     *
     * @return The event's server.
     */
    public Server getServer()
    {
        return this.server;
    }

    @Override
    public void call(ServerListener listener)
    {
        listener.serverClosing(this);
    }
}
