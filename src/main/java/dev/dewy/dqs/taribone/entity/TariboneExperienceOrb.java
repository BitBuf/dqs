package dev.dewy.dqs.taribone.entity;

import java.util.UUID;

/**
 * Represents an experience orb.
 */
public class TariboneExperienceOrb extends TariboneEntity
{
    private int count;

    public TariboneExperienceOrb(int id, UUID uuid)
    {
        super(id, uuid);
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
