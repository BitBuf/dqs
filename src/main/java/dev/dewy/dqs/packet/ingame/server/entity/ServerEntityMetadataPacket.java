package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.protocol.game.entity.metadata.EntityMetadata;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerEntityMetadataPacket extends MinecraftPacket
{
    private int entityId;
    private EntityMetadata metadata[];

    @SuppressWarnings("unused")
    private ServerEntityMetadataPacket()
    {
    }

    public ServerEntityMetadataPacket(int entityId, EntityMetadata metadata[])
    {
        this.entityId = entityId;
        this.metadata = metadata;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public EntityMetadata[] getMetadata()
    {
        return this.metadata;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }
}
