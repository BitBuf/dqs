package dev.dewy.dqs.protocol.game.world.effect;

import dev.dewy.dqs.utils.ObjectUtil;

public class RecordEffectData implements WorldEffectData
{
    private final int recordId;

    public RecordEffectData(int recordId)
    {
        this.recordId = recordId;
    }

    public int getRecordId()
    {
        return this.recordId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof RecordEffectData)) return false;

        RecordEffectData that = (RecordEffectData) o;
        return this.recordId == that.recordId;
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.recordId);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
