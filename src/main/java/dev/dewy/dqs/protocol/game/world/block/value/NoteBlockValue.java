package dev.dewy.dqs.protocol.game.world.block.value;

import dev.dewy.dqs.utils.ObjectUtil;

public class NoteBlockValue implements BlockValue
{
    private final int pitch;

    public NoteBlockValue(int pitch)
    {
        if (pitch < 0 || pitch > 24)
        {
            throw new IllegalArgumentException("Pitch must be between 0 and 24.");
        }

        this.pitch = pitch;
    }

    public int getPitch()
    {
        return this.pitch;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof NoteBlockValue)) return false;

        NoteBlockValue that = (NoteBlockValue) o;
        return this.pitch == that.pitch;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.pitch);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
