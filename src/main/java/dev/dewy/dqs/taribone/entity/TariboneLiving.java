package dev.dewy.dqs.taribone.entity;

import java.util.UUID;

public class TariboneLiving extends TariboneEntity
{
    protected double health;

    public TariboneLiving(int id, UUID uuid)
    {
        super(id, uuid);
    }

    public double getHealth()
    {
        return health;
    }

    public void setHealth(double health)
    {
        this.health = health;
    }
}
