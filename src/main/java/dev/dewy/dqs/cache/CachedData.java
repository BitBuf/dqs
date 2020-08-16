package dev.dewy.dqs.cache;

import dev.dewy.dqs.protocol.packet.Packet;

import java.util.function.Consumer;

public interface CachedData
{
    void getPackets(Consumer<Packet> consumer);

    void reset(boolean full);

    default String getSendingMessage()
    {
        return null;
    }
}
