package dev.dewy.dqs.protocol.game.world.effect;

import dev.dewy.dqs.utils.ObjectUtil;

public class BreakPotionEffectData implements WorldEffectData
{
    private final int potionId;

    public BreakPotionEffectData(int potionId)
    {
        this.potionId = potionId;
    }

    public int getPotionId()
    {
        return this.potionId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BreakPotionEffectData)) return false;

        BreakPotionEffectData that = (BreakPotionEffectData) o;
        return this.potionId == that.potionId;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.potionId);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
