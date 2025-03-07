package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.Effect;

import java.io.IOException;

public class ServerEntityRemoveEffectPacket extends MinecraftPacket
{
    private int entityId;
    private Effect effect;

    @SuppressWarnings("unused")
    private ServerEntityRemoveEffectPacket()
    {
    }

    public ServerEntityRemoveEffectPacket(int entityId, Effect effect)
    {
        this.entityId = entityId;
        this.effect = effect;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Effect getEffect()
    {
        return this.effect;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.effect = MagicValues.key(Effect.class, in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.effect));
    }
}
