package dev.dewy.dqs.taribone.pathing;

import dev.dewy.dqs.taribone.heuristic.Heuristic;
import dev.dewy.dqs.taribone.world.World;
import dev.dewy.dqs.taribone.world.physics.WorldPhysics;
import dev.dewy.dqs.utils.vector.Vector3i;

public interface PathSearchProvider
{
    PathSearch provideSearch(Vector3i start, Vector3i end);

    Heuristic getHeuristic();

    WorldPhysics getWorldPhysics();

    World getWorld();
}
