package dev.dewy.dqs.services.cache.data.entity;

import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;

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
