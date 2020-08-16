package dev.dewy.dqs.taribone.world.physics;

import dev.dewy.dqs.utils.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.taribone.world.World;
import dev.dewy.dqs.utils.vector.Vector3i;

public interface WorldPhysics
{
    Vector3i[] findWalkable(Vector3i from) throws ChunkNotLoadedException;

    Vector3i[] findAdjacent(Vector3i location);

    boolean canWalk(Vector3i from, Vector3i to) throws ChunkNotLoadedException;

    boolean canClimb(Vector3i location) throws ChunkNotLoadedException;

    boolean canStand(Vector3i location) throws ChunkNotLoadedException;

    World getWorld();
}
