package dev.dewy.dqs.packet;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

/**
 * A network packet.
 */
public interface Packet
{
    /**
     * Reads the packet from the given input buffer.
     *
     * @param in The input source to read from.
     * @throws IOException If an I/O error occurs.
     */
    void read(NetInput in) throws IOException;

    /**
     * Writes the packet to the given output buffer.
     *
     * @param out The output destination to write to.
     * @throws IOException If an I/O error occurs.
     */
    void write(NetOutput out) throws IOException;

    /**
     * Gets whether the packet has handling and writing priority.
     * If the result is true, the thread will wait for the packet to finish writing
     * when writing and the packet will be handled immediately after reading it.
     *
     * @return Whether the packet has priority.
     */
    boolean isPriority();
}
