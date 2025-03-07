package dev.dewy.dqs.protocol.core.event.session;

import dev.dewy.dqs.protocol.core.Session;

/**
 * Called when the session is disconnected.
 */
public class DisconnectedEvent implements SessionEvent
{
    private final Session session;
    private final String reason;
    private final Throwable cause;

    /**
     * Creates a new DisconnectedEvent instance.
     *
     * @param session Session being disconnected.
     * @param reason  Reason for the session to disconnect.
     */
    public DisconnectedEvent(Session session, String reason)
    {
        this(session, reason, null);
    }

    /**
     * Creates a new DisconnectedEvent instance.
     *
     * @param session Session being disconnected.
     * @param reason  Reason for the session to disconnect.
     * @param cause   Throwable that caused the disconnect.
     */
    public DisconnectedEvent(Session session, String reason, Throwable cause)
    {
        this.session = session;
        this.reason = reason;
        this.cause = cause;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession()
    {
        return this.session;
    }

    /**
     * Gets the reason given for the session disconnecting.
     *
     * @return The event's reason.
     */
    public String getReason()
    {
        return this.reason;
    }

    /**
     * Gets the Throwable responsible for the session disconnecting.
     *
     * @return The Throwable responsible for the disconnect, or null if the disconnect was not caused by a Throwable.
     */
    public Throwable getCause()
    {
        return this.cause;
    }

    @Override
    public void call(SessionListener listener)
    {
        listener.disconnected(this);
    }
}
