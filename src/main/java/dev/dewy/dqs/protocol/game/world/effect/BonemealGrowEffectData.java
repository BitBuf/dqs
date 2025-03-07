package dev.dewy.dqs.protocol.game.world.effect;

import dev.dewy.dqs.utils.ObjectUtil;

public class BonemealGrowEffectData implements WorldEffectData
{
    private final int particleCount;

    public BonemealGrowEffectData(int particleCount)
    {
        this.particleCount = particleCount;
    }

    public int getParticleCount()
    {
        return this.particleCount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BonemealGrowEffectData)) return false;

        BonemealGrowEffectData that = (BonemealGrowEffectData) o;
        return this.particleCount == that.particleCount;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.particleCount);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
