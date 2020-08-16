package dev.dewy.dqs.protocol.packet.ingame.server.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerCloseWindowPacket extends MinecraftPacket
{
    private int windowId;

    @SuppressWarnings("unused")
    private ServerCloseWindowPacket()
    {
    }

    public ServerCloseWindowPacket(int windowId)
    {
        this.windowId = windowId;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.windowId = in.readUnsignedByte();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
    }
}
