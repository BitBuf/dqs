package dev.dewy.dqs.nbt.conversion.builtin;

import dev.dewy.dqs.nbt.conversion.TagConverter;
import dev.dewy.dqs.nbt.tag.builtin.ByteTag;

/**
 * A converter that converts between ByteTag and byte.
 */
public class ByteTagConverter implements TagConverter<ByteTag, Byte>
{
    @Override
    public Byte convert(ByteTag tag)
    {
        return tag.getValue();
    }

    @Override
    public ByteTag convert(String name, Byte value)
    {
        return new ByteTag(name, value);
    }
}
