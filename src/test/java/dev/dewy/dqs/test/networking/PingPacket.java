package dev.dewy.dqs.test.networking;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.Packet;

import java.io.IOException;

public class PingPacket implements Packet
{
    private String id;

    @SuppressWarnings("unused")
    private PingPacket()
    {
    }

    public PingPacket(String id)
    {
        this.id = id;
    }

    public String getPingId()
    {
        return this.id;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.id = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.id);
    }

    @Override
    public boolean isPriority()
    {
        return false;
    }
}
