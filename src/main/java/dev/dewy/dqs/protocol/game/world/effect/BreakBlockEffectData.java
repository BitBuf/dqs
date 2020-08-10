package dev.dewy.dqs.protocol.game.world.effect;

import dev.dewy.dqs.protocol.game.world.block.BlockState;
import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class BreakBlockEffectData implements WorldEffectData
{
    private final BlockState blockState;

    public BreakBlockEffectData(BlockState blockState)
    {
        this.blockState = blockState;
    }

    public BlockState getBlockState()
    {
        return this.blockState;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BreakBlockEffectData)) return false;

        BreakBlockEffectData that = (BreakBlockEffectData) o;
        return Objects.equals(this.blockState, that.blockState);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.blockState);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
