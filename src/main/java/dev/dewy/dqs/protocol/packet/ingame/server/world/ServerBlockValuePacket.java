package dev.dewy.dqs.protocol.packet.ingame.server.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.core.NetUtil;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.world.block.value.*;

import java.io.IOException;

public class ServerBlockValuePacket extends MinecraftPacket
{
    private static final int NOTE_BLOCK = 25;
    private static final int STICKY_PISTON = 29;
    private static final int PISTON = 33;
    private static final int MOB_SPAWNER = 52;
    private static final int CHEST = 54;
    private static final int ENDER_CHEST = 130;
    private static final int TRAPPED_CHEST = 146;
    private static final int SHULKER_BOX_LOWER = 219;
    private static final int SHULKER_BOX_HIGHER = 234;

    private Position position;
    private BlockValueType type;
    private BlockValue value;
    private int blockId;

    @SuppressWarnings("unused")
    private ServerBlockValuePacket()
    {
    }

    public ServerBlockValuePacket(Position position, BlockValueType type, BlockValue value, int blockId)
    {
        this.position = position;
        this.type = type;
        this.value = value;
        this.blockId = blockId;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public BlockValueType getType()
    {
        return this.type;
    }

    public BlockValue getValue()
    {
        return this.value;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.position = NetUtil.readPosition(in);
        int type = in.readUnsignedByte();
        int value = in.readUnsignedByte();
        this.blockId = in.readVarInt() & 0xFFF;

        if (this.blockId == NOTE_BLOCK)
        {
            this.type = MagicValues.key(NoteBlockValueType.class, type);
            this.value = new NoteBlockValue(value);
        } else if (this.blockId == STICKY_PISTON || this.blockId == PISTON)
        {
            this.type = MagicValues.key(PistonValueType.class, type);
            this.value = MagicValues.key(PistonValue.class, value);
        } else if (this.blockId == MOB_SPAWNER)
        {
            this.type = MagicValues.key(MobSpawnerValueType.class, type);
            this.value = new MobSpawnerValue();
        } else if (this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST
                || (this.blockId >= SHULKER_BOX_LOWER && this.blockId <= SHULKER_BOX_HIGHER))
        {
            this.type = MagicValues.key(ChestValueType.class, type);
            this.value = new ChestValue(value);
        } else
        {
            this.type = MagicValues.key(GenericBlockValueType.class, type);
            this.value = new GenericBlockValue(value);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        NetUtil.writePosition(out, this.position);
        int type = 0;
        if (this.type instanceof NoteBlockValueType)
        {
            type = MagicValues.value(Integer.class, this.type);
        } else if (this.type instanceof PistonValueType)
        {
            type = MagicValues.value(Integer.class, this.type);
        } else if (this.type instanceof MobSpawnerValueType)
        {
            type = MagicValues.value(Integer.class, this.type);
        } else if (this.type instanceof ChestValueType)
        {
            type = MagicValues.value(Integer.class, this.type);
        } else if (this.type instanceof GenericBlockValueType)
        {
            type = MagicValues.value(Integer.class, this.type);
        }

        out.writeByte(type);
        int val = 0;
        if (this.value instanceof NoteBlockValue)
        {
            val = ((NoteBlockValue) this.value).getPitch();
        } else if (this.value instanceof PistonValue)
        {
            val = MagicValues.value(Integer.class, this.value);
        } else if (this.value instanceof MobSpawnerValue)
        {
            val = 0;
        } else if (this.value instanceof ChestValue)
        {
            val = ((ChestValue) this.value).getViewers();
        } else if (this.value instanceof GenericBlockValue)
        {
            val = ((GenericBlockValue) this.value).getValue();
        }

        out.writeByte(val);
        out.writeVarInt(this.blockId & 4095);
    }
}
