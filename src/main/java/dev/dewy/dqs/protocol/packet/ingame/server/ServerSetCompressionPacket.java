package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerSetCompressionPacket extends MinecraftPacket
{
    private int threshold;

    @SuppressWarnings("unused")
    private ServerSetCompressionPacket()
    {
    }

    public ServerSetCompressionPacket(int threshold)
    {
        this.threshold = threshold;
    }

    public int getThreshold()
    {
        return this.threshold;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.threshold = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.threshold);
    }

    @Override
    public boolean isPriority()
    {
        return true;
    }
}
