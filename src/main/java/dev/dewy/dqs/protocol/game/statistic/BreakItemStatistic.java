package dev.dewy.dqs.protocol.game.statistic;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class BreakItemStatistic implements Statistic
{
    private final String id;

    public BreakItemStatistic(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BreakItemStatistic)) return false;

        BreakItemStatistic that = (BreakItemStatistic) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.id);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
