package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerEntityCollectItemPacket extends MinecraftPacket
{
    private int collectedEntityId;
    private int collectorEntityId;
    private int itemCount;

    @SuppressWarnings("unused")
    private ServerEntityCollectItemPacket()
    {
    }

    public ServerEntityCollectItemPacket(int collectedEntityId, int collectorEntityId, int itemCount)
    {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.itemCount = itemCount;
    }

    public int getCollectedEntityId()
    {
        return this.collectedEntityId;
    }

    public int getCollectorEntityId()
    {
        return this.collectorEntityId;
    }

    public int getItemCount()
    {
        return this.itemCount;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.collectedEntityId = in.readVarInt();
        this.collectorEntityId = in.readVarInt();
        this.itemCount = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.collectedEntityId);
        out.writeVarInt(this.collectorEntityId);
        out.writeVarInt(this.itemCount);
    }
}
