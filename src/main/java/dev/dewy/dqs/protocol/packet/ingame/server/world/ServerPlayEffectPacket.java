package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.world.block.BlockState;
import dev.dewy.dqs.protocol.game.world.effect.*;

import java.io.IOException;

public class ServerPlayEffectPacket extends MinecraftPacket
{
    private WorldEffect effect;
    private Position position;
    private WorldEffectData data;
    private boolean broadcast;

    @SuppressWarnings("unused")
    private ServerPlayEffectPacket()
    {
    }

    public ServerPlayEffectPacket(WorldEffect effect, Position position, WorldEffectData data)
    {
        this(effect, position, data, false);
    }

    public ServerPlayEffectPacket(WorldEffect effect, Position position, WorldEffectData data, boolean broadcast)
    {
        this.effect = effect;
        this.position = position;
        this.data = data;
        this.broadcast = broadcast;
    }

    public WorldEffect getEffect()
    {
        return this.effect;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public WorldEffectData getData()
    {
        return this.data;
    }

    public boolean getBroadcast()
    {
        return this.broadcast;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.effect = MagicValues.key(WorldEffect.class, in.readInt());
        this.position = NetUtil.readPosition(in);
        int value = in.readInt();
        if (this.effect == SoundEffect.RECORD)
        {
            this.data = new RecordEffectData(value);
        } else if (this.effect == ParticleEffect.SMOKE)
        {
            this.data = MagicValues.key(SmokeEffectData.class, value % 9);
        } else if (this.effect == ParticleEffect.BREAK_BLOCK)
        {
            this.data = new BreakBlockEffectData(new BlockState(value & 4095, (value >> 12) & 255));
        } else if (this.effect == ParticleEffect.BREAK_SPLASH_POTION)
        {
            this.data = new BreakPotionEffectData(value);
        } else if (this.effect == ParticleEffect.BONEMEAL_GROW)
        {
            this.data = new BonemealGrowEffectData(value);
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeInt(MagicValues.value(Integer.class, this.effect));
        NetUtil.writePosition(out, this.position);
        int value = 0;
        if (this.data instanceof RecordEffectData)
        {
            value = ((RecordEffectData) this.data).getRecordId();
        } else if (this.data instanceof SmokeEffectData)
        {
            value = MagicValues.value(Integer.class, this.data);
        } else if (this.data instanceof BreakBlockEffectData)
        {
            value = (((BreakBlockEffectData) this.data).getBlockState().getId() & 4095) | ((((BreakBlockEffectData) this.data).getBlockState().getData() & 255) << 12);
        } else if (this.data instanceof BreakPotionEffectData)
        {
            value = ((BreakPotionEffectData) this.data).getPotionId();
        } else if (this.data instanceof BonemealGrowEffectData)
        {
            value = ((BonemealGrowEffectData) this.data).getParticleCount();
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }
}
