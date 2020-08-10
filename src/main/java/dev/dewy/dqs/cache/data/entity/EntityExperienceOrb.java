package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;

import java.util.function.Consumer;

public class EntityExperienceOrb extends Entity
{
    protected int exp;

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerSpawnExpOrbPacket(
                this.getEntityId(),
                this.getX(),
                this.getY(),
                this.getZ(),
                this.exp
        ));
        super.addPackets(consumer);
    }

    public int getExp()
    {
        return this.exp;
    }

    public EntityExperienceOrb setExp(int exp)
    {
        this.exp = exp;
        return this;
    }
}
