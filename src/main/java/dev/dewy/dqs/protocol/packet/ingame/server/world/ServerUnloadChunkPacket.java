package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerUnloadChunkPacket extends MinecraftPacket
{
    private int x;
    private int z;

    @SuppressWarnings("unused")
    private ServerUnloadChunkPacket()
    {
    }

    public ServerUnloadChunkPacket(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public int getX()
    {
        return this.x;
    }

    public int getZ()
    {
        return this.z;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.x = in.readInt();
        this.z = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeInt(this.x);
        out.writeInt(this.z);
    }
}
