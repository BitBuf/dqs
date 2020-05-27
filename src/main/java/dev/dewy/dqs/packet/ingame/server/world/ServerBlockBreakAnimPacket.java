package dev.dewy.dqs.packet.ingame.server.world;

import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.entity.player.BlockBreakStage;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerBlockBreakAnimPacket extends MinecraftPacket
{
    private int breakerEntityId;
    private Position position;
    private BlockBreakStage stage;

    @SuppressWarnings("unused")
    private ServerBlockBreakAnimPacket()
    {
    }

    public ServerBlockBreakAnimPacket(int breakerEntityId, Position position, BlockBreakStage stage)
    {
        this.breakerEntityId = breakerEntityId;
        this.position = position;
        this.stage = stage;
    }

    public int getBreakerEntityId()
    {
        return this.breakerEntityId;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public BlockBreakStage getStage()
    {
        return this.stage;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.breakerEntityId = in.readVarInt();
        this.position = NetUtil.readPosition(in);
        try
        {
            this.stage = MagicValues.key(BlockBreakStage.class, in.readUnsignedByte());
        } catch (IllegalArgumentException e)
        {
            this.stage = BlockBreakStage.RESET;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.breakerEntityId);
        NetUtil.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.stage));
    }
}
