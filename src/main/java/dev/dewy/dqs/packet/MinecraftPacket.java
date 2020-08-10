package dev.dewy.dqs.packet;

import dev.dewy.dqs.utils.ObjectUtil;

public abstract class MinecraftPacket implements Packet
{
    @Override
    public boolean isPriority()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
