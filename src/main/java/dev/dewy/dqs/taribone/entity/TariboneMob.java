package dev.dewy.dqs.taribone.entity;

import dev.dewy.dqs.protocol.game.entity.type.MobType;

import java.util.UUID;

public class TariboneMob extends TariboneEntity
{
    protected final MobType type;

    public TariboneMob(int id, UUID uuid, MobType type)
    {
        super(id, uuid);

        this.type = type;
    }

    public MobType getType()
    {
        return type;
    }
}
