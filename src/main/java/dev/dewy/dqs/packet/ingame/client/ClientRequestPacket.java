package dev.dewy.dqs.packet.ingame.client;

import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.ClientRequest;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ClientRequestPacket extends MinecraftPacket
{
    private ClientRequest request;

    @SuppressWarnings("unused")
    private ClientRequestPacket()
    {
    }

    public ClientRequestPacket(ClientRequest request)
    {
        this.request = request;
    }

    public ClientRequest getRequest()
    {
        return this.request;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.request = MagicValues.key(ClientRequest.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(MagicValues.value(Integer.class, this.request));
    }
}
