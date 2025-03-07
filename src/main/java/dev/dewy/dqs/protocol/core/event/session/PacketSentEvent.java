package dev.dewy.dqs.protocol.core.event.session;

import dev.dewy.dqs.protocol.core.Session;
import dev.dewy.dqs.protocol.packet.Packet;

/**
 * Called when the session has sent a packet.
 */
public class PacketSentEvent implements SessionEvent
{
    private final Session session;
    private final Packet packet;

    /**
     * Creates a new PacketSentEvent instance.
     *
     * @param session Session sending the packet.
     * @param packet  Packet being sent.
     */
    public PacketSentEvent(Session session, Packet packet)
    {
        this.session = session;
        this.packet = packet;
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
     * Gets the packet involved in this event as the required type.
     *
     * @param <T> Type of the packet.
     * @return The event's packet as the required type.
     * @throws IllegalStateException If the packet's value isn't of the required type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Packet> T getPacket()
    {
        try
        {
            return (T) this.packet;
        } catch (ClassCastException e)
        {
            throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
        }
    }

    @Override
    public void call(SessionListener listener)
    {
        listener.packetSent(this);
    }
}
