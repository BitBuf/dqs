package dev.dewy.dqs.protocol.packet.ingame.server.entity.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;

import java.io.IOException;

public class ServerPlayerUseBedPacket extends MinecraftPacket
{
    private int entityId;
    private Position position;

    @SuppressWarnings("unused")
    private ServerPlayerUseBedPacket()
    {
    }

    public ServerPlayerUseBedPacket(int entityId, Position position)
    {
        this.entityId = entityId;
        this.position = position;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Position getPosition()
    {
        return this.position;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.position = NetUtil.readPosition(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        NetUtil.writePosition(out, this.position);
    }
}
