package dev.dewy.dqs.utils.nbt.conversion;

import dev.dewy.dqs.utils.nbt.tag.builtin.Tag;

/**
 * A converter that converts between a tag type and a value type. A converted tag will have its value and all children converted to raw types and vice versa.
 *
 * @param <T> Tag type.
 * @param <V> Value type.
 */
public interface TagConverter<T extends Tag, V>
{
    /**
     * Converts a tag to a value.
     *
     * @param tag Tag to convert.
     * @return The converted value.
     */
    V convert(T tag);

    /**
     * Converts a value to a tag.
     *
     * @param name  Name of the tag.
     * @param value Value to convert.
     * @return The converted tag.
     */
    T convert(String name, V value);
}
