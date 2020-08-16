package dev.dewy.dqs.protocol.packet.ingame.client;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

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
