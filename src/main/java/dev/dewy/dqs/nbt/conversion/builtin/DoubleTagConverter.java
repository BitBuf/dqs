package dev.dewy.dqs.nbt.conversion.builtin;

import dev.dewy.dqs.nbt.conversion.TagConverter;
import dev.dewy.dqs.nbt.tag.builtin.DoubleTag;

/**
 * A converter that converts between DoubleTag and double.
 */
public class DoubleTagConverter implements TagConverter<DoubleTag, Double>
{
    @Override
    public Double convert(DoubleTag tag)
    {
        return tag.getValue();
    }

    @Override
    public DoubleTag convert(String name, Double value)
    {
        return new DoubleTag(name, value);
    }
}
