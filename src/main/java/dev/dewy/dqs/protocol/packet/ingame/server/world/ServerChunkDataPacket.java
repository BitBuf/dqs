package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.utils.io.stream.StreamNetOutput;
import dev.dewy.dqs.utils.nbt.tag.builtin.CompoundTag;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.chunk.Column;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerChunkDataPacket extends MinecraftPacket
{
    private Column column;

    @SuppressWarnings("unused")
    private ServerChunkDataPacket()
    {
    }

    public ServerChunkDataPacket(Column column)
    {
        this.column = column;
    }

    public Column getColumn()
    {
        return this.column;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        int x = in.readInt();
        int z = in.readInt();
        boolean fullChunk = in.readBoolean();
        int chunkMask = in.readVarInt();
        byte[] data = in.readBytes(in.readVarInt());
        CompoundTag[] tileEntities = new CompoundTag[in.readVarInt()];
        for (int i = 0; i < tileEntities.length; i++)
        {
            tileEntities[i] = NetUtil.readNBT(in);
        }

        this.column = NetUtil.readColumn(data, x, z, fullChunk, false, chunkMask, tileEntities);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        NetOutput netOut = new StreamNetOutput(byteOut);
        int mask = NetUtil.writeColumn(netOut, this.column, this.column.hasBiomeData(), this.column.hasSkylight());

        out.writeInt(this.column.getX());
        out.writeInt(this.column.getZ());
        out.writeBoolean(this.column.hasBiomeData());
        out.writeVarInt(mask);
        out.writeVarInt(byteOut.size());
        out.writeBytes(byteOut.toByteArray(), byteOut.size());
        out.writeVarInt(this.column.getTileEntities().length);
        for (CompoundTag tag : this.column.getTileEntities())
        {
            NetUtil.writeNBT(out, tag);
        }
    }
}
