package dev.dewy.dqs.protocol.packet.ingame.client;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.ResourcePackStatus;

import java.io.IOException;

public class ClientResourcePackStatusPacket extends MinecraftPacket
{
    private ResourcePackStatus status;

    @SuppressWarnings("unused")
    private ClientResourcePackStatusPacket()
    {
    }

    public ClientResourcePackStatusPacket(ResourcePackStatus status)
    {
        this.status = status;
    }

    public ResourcePackStatus getStatus()
    {
        return this.status;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.status = MagicValues.key(ResourcePackStatus.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(MagicValues.value(Integer.class, this.status));
    }
}
