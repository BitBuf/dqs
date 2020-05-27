package dev.dewy.dqs.packet.ingame.server.entity;

import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.Animation;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ServerEntityAnimationPacket extends MinecraftPacket
{
    private int entityId;
    private Animation animation;

    @SuppressWarnings("unused")
    private ServerEntityAnimationPacket()
    {
    }

    public ServerEntityAnimationPacket(int entityId, Animation animation)
    {
        this.entityId = entityId;
        this.animation = animation;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Animation getAnimation()
    {
        return this.animation;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.animation = MagicValues.key(Animation.class, in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.animation));
    }
}
