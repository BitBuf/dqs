package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.world.block.BlockChangeRecord;

import java.io.IOException;

public class ServerBlockChangePacket extends MinecraftPacket
{
    private BlockChangeRecord record;

    @SuppressWarnings("unused")
    private ServerBlockChangePacket()
    {
    }

    public ServerBlockChangePacket(BlockChangeRecord record)
    {
        this.record = record;
    }

    public BlockChangeRecord getRecord()
    {
        return this.record;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.record = new BlockChangeRecord(NetUtil.readPosition(in), NetUtil.readBlockState(in));
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        NetUtil.writePosition(out, this.record.getPosition());
        NetUtil.writeBlockState(out, this.record.getBlock());
    }
}
