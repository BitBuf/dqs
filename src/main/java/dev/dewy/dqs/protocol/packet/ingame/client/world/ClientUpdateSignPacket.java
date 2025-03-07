package dev.dewy.dqs.protocol.packet.ingame.client.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;

import java.io.IOException;

public class ClientUpdateSignPacket extends MinecraftPacket
{
    private Position position;
    private String[] lines;

    @SuppressWarnings("unused")
    private ClientUpdateSignPacket()
    {
    }

    public ClientUpdateSignPacket(Position position, String[] lines)
    {
        if (lines.length != 4)
        {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
        }

        this.position = position;
        this.lines = lines;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public String[] getLines()
    {
        return this.lines;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.position = NetUtil.readPosition(in);
        this.lines = new String[4];
        for (int count = 0; count < this.lines.length; count++)
        {
            this.lines[count] = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        NetUtil.writePosition(out, this.position);
        for (String line : this.lines)
        {
            out.writeString(line);
        }
    }
}
