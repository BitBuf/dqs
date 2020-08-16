package dev.dewy.dqs.protocol.packet.status.client;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class StatusQueryPacket extends MinecraftPacket
{
    public StatusQueryPacket()
    {
    }

    @Override
    public void read(NetInput in) throws IOException
    {
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
    }
}
