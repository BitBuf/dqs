package dev.dewy.dqs.packet.status.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class StatusPingPacket extends MinecraftPacket
{
    private long time;

    @SuppressWarnings("unused")
    private StatusPingPacket()
    {
    }

    public StatusPingPacket(long time)
    {
        this.time = time;
    }

    public long getPingTime()
    {
        return this.time;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.time = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeLong(this.time);
    }
}
