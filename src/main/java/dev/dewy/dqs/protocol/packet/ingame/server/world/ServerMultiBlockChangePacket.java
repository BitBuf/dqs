package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.world.block.BlockChangeRecord;
import dev.dewy.dqs.protocol.game.world.block.BlockState;

import java.io.IOException;

public class ServerMultiBlockChangePacket extends MinecraftPacket
{
    private BlockChangeRecord[] records;

    @SuppressWarnings("unused")
    private ServerMultiBlockChangePacket()
    {
    }

    public ServerMultiBlockChangePacket(BlockChangeRecord... records)
    {
        if (records == null || records.length == 0)
        {
            throw new IllegalArgumentException("Records must contain at least 1 value.");
        }

        this.records = records;
    }

    public BlockChangeRecord[] getRecords()
    {
        return this.records;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        int chunkX = in.readInt();
        int chunkZ = in.readInt();
        this.records = new BlockChangeRecord[in.readVarInt()];
        for (int index = 0; index < this.records.length; index++)
        {
            short pos = in.readShort();
            BlockState block = NetUtil.readBlockState(in);
            int x = (chunkX << 4) + (pos >> 12 & 15);
            int y = pos & 255;
            int z = (chunkZ << 4) + (pos >> 8 & 15);
            this.records[index] = new BlockChangeRecord(new Position(x, y, z), block);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        int chunkX = this.records[0].getPosition().getX() >> 4;
        int chunkZ = this.records[0].getPosition().getZ() >> 4;
        out.writeInt(chunkX);
        out.writeInt(chunkZ);
        out.writeVarInt(this.records.length);
        for (BlockChangeRecord record : this.records)
        {
            out.writeShort((record.getPosition().getX() - (chunkX << 4)) << 12 | (record.getPosition().getZ() - (chunkZ << 4)) << 8 | record.getPosition().getY());
            NetUtil.writeBlockState(out, record.getBlock());
        }
    }
}
