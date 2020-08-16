package dev.dewy.dqs.protocol.packet.ingame.client.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ClientCloseWindowPacket extends MinecraftPacket
{
    private int windowId;

    @SuppressWarnings("unused")
    private ClientCloseWindowPacket()
    {
    }

    public ClientCloseWindowPacket(int windowId)
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
        this.windowId = in.readByte();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
    }
}
