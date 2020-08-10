package dev.dewy.dqs.protocol.game.world.sound;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class CustomSound implements Sound
{
    private final String name;

    public CustomSound(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof CustomSound)) return false;

        CustomSound that = (CustomSound) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.name);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
