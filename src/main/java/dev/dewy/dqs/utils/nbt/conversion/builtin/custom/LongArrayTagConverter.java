package dev.dewy.dqs.utils.nbt.conversion.builtin.custom;

import dev.dewy.dqs.utils.nbt.conversion.TagConverter;
import dev.dewy.dqs.utils.nbt.tag.builtin.custom.LongArrayTag;

/**
 * A converter that converts between LongArrayTag and long[].
 */
public class LongArrayTagConverter implements TagConverter<LongArrayTag, long[]>
{
    @Override
    public long[] convert(LongArrayTag tag)
    {
        return tag.getValue();
    }

    @Override
    public LongArrayTag convert(String name, long[] value)
    {
        return new LongArrayTag(name, value);
    }
}
