package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.entity.type.PaintingType;
import dev.dewy.dqs.protocol.game.entity.type.object.HangingDirection;

import java.util.function.Consumer;

import static net.daporkchop.lib.common.math.PMath.floorI;

public class EntityPainting extends Entity
{
    protected PaintingType paintingType;
    protected HangingDirection direction;

    @Override
    public void addPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerSpawnPaintingPacket(
                this.entityId,
                this.uuid,
                this.paintingType,
                new Position(
                        floorI(this.x),
                        floorI(this.y),
                        floorI(this.z)
                ),
                this.direction
        ));
        super.addPackets(consumer);
    }

    public PaintingType getPaintingType()
    {
        return this.paintingType;
    }

    public EntityPainting setPaintingType(PaintingType paintingType)
    {
        this.paintingType = paintingType;
        return this;
    }

    public HangingDirection getDirection()
    {
        return this.direction;
    }

    public EntityPainting setDirection(HangingDirection direction)
    {
        this.direction = direction;
        return this;
    }
}
