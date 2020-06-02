package dev.dewy.dqs.protocol.game.entity.attribute;

import dev.dewy.dqs.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Attribute
{
    private final AttributeType type;
    private final double value;
    private final List<AttributeModifier> modifiers;

    public Attribute(AttributeType type)
    {
        this(type, type.getDefault());
    }

    public Attribute(AttributeType type, double value)
    {
        this(type, value, new ArrayList<AttributeModifier>());
    }

    public Attribute(AttributeType type, double value, List<AttributeModifier> modifiers)
    {
        this.type = type;
        this.value = value;
        this.modifiers = modifiers;
    }

    public AttributeType getType()
    {
        return this.type;
    }

    public double getValue()
    {
        return this.value;
    }

    public List<AttributeModifier> getModifiers()
    {
        return new ArrayList<AttributeModifier>(this.modifiers);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;

        Attribute that = (Attribute) o;
        return this.type == that.type &&
                this.value == that.value &&
                Objects.equals(this.modifiers, that.modifiers);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.type, this.value, this.modifiers);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
