package dev.dewy.dqs.nbt.conversion.builtin.custom;

import dev.dewy.dqs.nbt.conversion.TagConverter;
import dev.dewy.dqs.nbt.tag.builtin.custom.SerializableArrayTag;

import java.io.Serializable;

/**
 * A converter that converts between SerializableArrayTag and Serializable[].
 */
public class SerializableArrayTagConverter implements TagConverter<SerializableArrayTag, Serializable[]>
{
    @Override
    public Serializable[] convert(SerializableArrayTag tag)
    {
        return tag.getValue();
    }

    @Override
    public SerializableArrayTag convert(String name, Serializable[] value)
    {
        return new SerializableArrayTag(name, value);
    }
}
