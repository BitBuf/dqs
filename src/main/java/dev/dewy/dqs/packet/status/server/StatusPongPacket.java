package dev.dewy.dqs.packet.status.server;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class StatusPongPacket extends MinecraftPacket
{
    private long time;

    @SuppressWarnings("unused")
    private StatusPongPacket()
    {
    }

    public StatusPongPacket(long time)
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
