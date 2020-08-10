package dev.dewy.dqs.protocol.game.statistic;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class CustomStatistic implements Statistic
{
    private final String name;

    public CustomStatistic(String name)
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
        if (!(o instanceof CustomStatistic)) return false;

        CustomStatistic that = (CustomStatistic) o;
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
