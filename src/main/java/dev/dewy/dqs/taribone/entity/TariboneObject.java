package dev.dewy.dqs.taribone.entity;

import dev.dewy.dqs.protocol.game.entity.type.object.ObjectType;

import java.util.UUID;

public class TariboneObject extends TariboneEntity
{
    protected int data;
    protected ObjectType type;

    public TariboneObject(int id, UUID uuid)
    {
        super(id, uuid);
    }

    public int getData()
    {
        return data;
    }

    public void setData(int data)
    {
        this.data = data;
    }

    public ObjectType getType()
    {
        return type;
    }

    public void setType(ObjectType type)
    {
        this.type = type;
    }
}
