package dev.dewy.dqs.protocol.game.world.notify;

import dev.dewy.dqs.utils.ObjectUtil;

public class RainStrengthValue implements ClientNotificationValue
{
    private final float strength;

    public RainStrengthValue(float strength)
    {
        if (strength > 1)
        {
            strength = 1;
        }

        if (strength < 0)
        {
            strength = 0;
        }

        this.strength = strength;
    }

    public float getStrength()
    {
        return this.strength;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof RainStrengthValue)) return false;

        RainStrengthValue that = (RainStrengthValue) o;
        return Float.compare(this.strength, that.strength) == 0;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.strength);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
