package dev.dewy.dqs.protocol.game.statistic;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class CraftItemStatistic implements Statistic
{
    private final String id;

    public CraftItemStatistic(String id)
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
        if (!(o instanceof CraftItemStatistic)) return false;

        CraftItemStatistic that = (CraftItemStatistic) o;
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
