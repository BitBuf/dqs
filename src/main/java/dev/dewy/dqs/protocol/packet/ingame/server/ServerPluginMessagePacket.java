package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerPluginMessagePacket extends MinecraftPacket
{
    private String channel;
    private byte[] data;

    @SuppressWarnings("unused")
    private ServerPluginMessagePacket()
    {
    }

    public ServerPluginMessagePacket(String channel, byte[] data)
    {
        this.channel = channel;
        this.data = data;
    }

    public String getChannel()
    {
        return this.channel;
    }

    public byte[] getData()
    {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.channel = in.readString();
        this.data = in.readBytes(in.available());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }
}
