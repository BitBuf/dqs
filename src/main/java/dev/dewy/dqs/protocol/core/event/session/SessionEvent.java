package dev.dewy.dqs.protocol.core.event.session;

/**
 * An event relating to sessions.
 */
public interface SessionEvent
{
    /**
     * Calls the event.
     *
     * @param listener Listener to call the event on.
     */
    void call(SessionListener listener);
}
