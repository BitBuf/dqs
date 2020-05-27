package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

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
