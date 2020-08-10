package dev.dewy.dqs.taribone.heuristic;

import dev.dewy.dqs.utils.vector.Vector3d;

public class ManhattanHeuristic implements Heuristic
{
    @Override
    public double calculateCost(Vector3d from, Vector3d to)
    {
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) + Math.abs(from.getZ() - to.getZ());
    }
}
