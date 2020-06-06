package dev.dewy.dqs.taribone.world.block;

import com.google.common.base.Preconditions;
import dev.dewy.dqs.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.protocol.game.chunk.BlockStorage;
import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.protocol.game.world.block.BlockState;
import dev.dewy.dqs.taribone.world.World;
import dev.dewy.dqs.taribone.world.chunk.Chunk;
import dev.dewy.dqs.utils.vector.Vector3i;

import java.util.Objects;

public class Block
{
    private final int x, y, z;
    private final Chunk chunk;

    public Block(int x, int y, int z, Chunk chunk)
    {
        Preconditions.checkArgument(y >= 0, "Argument was %s but expected nonnegative", y);
        Preconditions.checkArgument(y < 256, "Argument was %s but expected lower than 256", y);
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public Vector3i getLocation()
    {
        return new Vector3i(x, y, z);
    }

    public Chunk getChunk()
    {
        return chunk;
    }

    public World getWorld()
    {
        return chunk.getWorld();
    }

    public Block getRelative(BlockFace face) throws ChunkNotLoadedException
    {
        return getWorld().getBlockAt(getLocation().add(face.getOffset()));
    }

    public int getTypeId()
    {
        return getInternalState().getId();
    }

    public void setTypeId(int newType)
    {
        setInternalState(new BlockState(newType, getData()));
    }

    public int getData()
    {
        return getInternalState().getData();
    }

    public void setData(byte newData)
    {
        setInternalState(new BlockState(getTypeId(), newData));
    }

    public void setTypeIdAndData(int newType, byte newData)
    {
        setInternalState(new BlockState(newType, newData));
    }

    public Material getMaterial()
    {
        return Material.getById(this.getTypeId());
    }

    public Block getRelative(int x, int y, int z) throws ChunkNotLoadedException
    {
        return getWorld().getBlockAt(this.x + x, this.y + y, this.z + z);
    }

    public Block getRelative(Vector3i offset) throws ChunkNotLoadedException
    {
        return getRelative(offset.getX(), offset.getY(), offset.getZ());
    }

    private BlockStorage getInternalStorage()
    {
        Column handle = chunk.getHandle();

        // TODO: Test this
        int index = Math.floorDiv(y, 16);
        //GlobalLogger.getLogger().info("Get Internal Storage - y: " + y + ", index: " + index);
        if (index > 15)
        {
//            Constants.TARIBONE_LOG.warn("How did this happen: (" + x + "," + y + "," + z + ")");
        }

        dev.dewy.dqs.protocol.game.chunk.Chunk[] sections = handle.getChunks();

        if (sections[index] == null)
        {
            //GlobalLogger.getLogger().info("Making new chunk section for air chunk section: (" + handle.getX() + "," + index + "," + handle.getZ() + ")");
            sections[index] = new dev.dewy.dqs.protocol.game.chunk.Chunk(handle.hasSkylight());
        }

        return sections[index].getBlocks();

    }

    private BlockState getInternalState()
    {
        int locX = Math.floorMod(x, 16);
        int locY = Math.floorMod(y, 16);
        int locZ = Math.floorMod(z, 16);

        return getInternalStorage().get(
                locX,
                locY,
                locZ);
    }

    public void setInternalState(BlockState newState)
    {
        int locX = Math.floorMod(x, 16);
        int locY = Math.floorMod(y, 16);
        int locZ = Math.floorMod(z, 16);
        //GlobalLogger.getLogger().info("Set internal state: (" + x + "," + y + "," + z + ") -> (" + locX + "," + locY + "," + locZ + ")");

        getInternalStorage().set(
                locX,
                locY,
                locZ,
                newState);
    }

    @Override
    public int hashCode()
    {
        return 7;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Block other = (Block) obj;
        if (this.x != other.x)
        {
            return false;
        }
        if (this.y != other.y)
        {
            return false;
        }
        if (this.z != other.z)
        {
            return false;
        }
        return Objects.equals(this.chunk, other.chunk);
    }

}
