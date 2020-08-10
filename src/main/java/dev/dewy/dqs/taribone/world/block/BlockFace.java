package dev.dewy.dqs.taribone.world.block;

import dev.dewy.dqs.utils.vector.Vector3i;

import java.util.EnumMap;
import java.util.HashMap;

public enum BlockFace
{
    BOTTOM(0, -1, 0),
    TOP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    SPECIAL(0, 0, 0);

    private static final HashMap<Vector3i, BlockFace> VECTOR3I_TO_BLOCKFACE = new HashMap<>();
    private static final EnumMap<BlockFace, BlockFace> OPPOSITE = new EnumMap<>(BlockFace.class);

    static
    {
        for (BlockFace face : values())
        {
            VECTOR3I_TO_BLOCKFACE.put(face.offset, face);
        }
        OPPOSITE.put(BOTTOM, TOP);
        OPPOSITE.put(TOP, BOTTOM);
        OPPOSITE.put(NORTH, SOUTH);
        OPPOSITE.put(SOUTH, NORTH);
        OPPOSITE.put(WEST, EAST);
        OPPOSITE.put(EAST, WEST);
        OPPOSITE.put(SPECIAL, SPECIAL);
    }


    private final Vector3i offset;

    BlockFace(int modX, int modY, int modZ)
    {
        this.offset = new Vector3i(modX, modY, modZ);
    }

    public static BlockFace forUnitVector(Vector3i vec)
    {
        return VECTOR3I_TO_BLOCKFACE.get(vec);
    }

    public Vector3i getOffset()
    {
        return offset;
    }

    public BlockFace getOpposite()
    {
        return OPPOSITE.get(this);
    }

    public dev.dewy.dqs.protocol.game.world.block.BlockFace getInternalFace()
    {
        return dev.dewy.dqs.protocol.game.world.block.BlockFace.values()[ordinal()];
    }
}
