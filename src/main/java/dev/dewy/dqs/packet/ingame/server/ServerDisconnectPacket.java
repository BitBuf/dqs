package dev.dewy.dqs.packet.ingame.server;

import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerDisconnectPacket extends MinecraftPacket
{
    private String message;

    @SuppressWarnings("unused")
    private ServerDisconnectPacket()
    {
    }

    public ServerDisconnectPacket(String text, boolean escape)
    {
        this.message = escape ? ServerChatPacket.escapeText(text) : text;
    }

    public String getReason()
    {
        return this.message;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.message = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.message);
    }

    @Override
    public boolean isPriority()
    {
        return true;
    }
}
