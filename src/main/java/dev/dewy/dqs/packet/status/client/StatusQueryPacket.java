package dev.dewy.dqs.packet.status.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

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
