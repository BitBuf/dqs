package dev.dewy.dqs.protocol.game.entity.type.object;

import dev.dewy.dqs.utils.ObjectUtil;

public class SplashPotionData implements ObjectData
{
    private final int potionData;

    public SplashPotionData(int potionData)
    {
        this.potionData = potionData;
    }

    public int getPotionData()
    {
        return this.potionData;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof SplashPotionData)) return false;

        SplashPotionData that = (SplashPotionData) o;
        return this.potionData == that.potionData;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.potionData);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
