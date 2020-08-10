package dev.dewy.dqs.packet.ingame.client.world;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class ClientTeleportConfirmPacket extends MinecraftPacket
{
    private int id;

    @SuppressWarnings("unused")
    private ClientTeleportConfirmPacket()
    {
    }

    public ClientTeleportConfirmPacket(int id)
    {
        this.id = id;
    }

    public int getTeleportId()
    {
        return this.id;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.id = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.id);
    }
}
