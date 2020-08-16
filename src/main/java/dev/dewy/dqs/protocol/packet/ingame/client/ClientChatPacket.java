package dev.dewy.dqs.protocol.packet.ingame.client;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

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
