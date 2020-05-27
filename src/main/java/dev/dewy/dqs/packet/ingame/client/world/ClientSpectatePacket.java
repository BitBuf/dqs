package dev.dewy.dqs.packet.ingame.client.world;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;
import java.util.UUID;

public class ClientSpectatePacket extends MinecraftPacket
{
    private UUID target;

    @SuppressWarnings("unused")
    private ClientSpectatePacket()
    {
    }

    public ClientSpectatePacket(UUID target)
    {
        this.target = target;
    }

    public UUID getTarget()
    {
        return this.target;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.target = in.readUUID();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeUUID(this.target);
    }
}
