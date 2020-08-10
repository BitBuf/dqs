package dev.dewy.dqs.packet.ingame.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class ClientChatPacket extends MinecraftPacket
{
    private String message;

    @SuppressWarnings("unused")
    private ClientChatPacket()
    {
    }

    public ClientChatPacket(String message)
    {
        this.message = message;
    }

    public String getMessage()
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
}
