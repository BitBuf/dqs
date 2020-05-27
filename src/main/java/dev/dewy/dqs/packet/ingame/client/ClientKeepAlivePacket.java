package dev.dewy.dqs.packet.ingame.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class ClientKeepAlivePacket extends MinecraftPacket
{
    private long id;

    @SuppressWarnings("unused")
    private ClientKeepAlivePacket()
    {
    }

    public ClientKeepAlivePacket(long id)
    {
        this.id = id;
    }

    public long getPingId()
    {
        return this.id;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.id = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeLong(this.id);
    }
}
