package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;

import java.io.IOException;

public class ServerOpenTileEntityEditorPacket extends MinecraftPacket
{
    private Position position;

    @SuppressWarnings("unused")
    private ServerOpenTileEntityEditorPacket()
    {
    }

    public ServerOpenTileEntityEditorPacket(Position position)
    {
        this.position = position;
    }

    public Position getPosition()
    {
        return this.position;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.position = NetUtil.readPosition(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        NetUtil.writePosition(out, this.position);
    }
}
