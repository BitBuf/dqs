package dev.dewy.dqs.protocol.packet.login.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerChatPacket;

import java.io.IOException;

public class LoginDisconnectPacket extends MinecraftPacket
{
    private String message;

    @SuppressWarnings("unused")
    private LoginDisconnectPacket()
    {
    }

    public LoginDisconnectPacket(String text, boolean escape)
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
