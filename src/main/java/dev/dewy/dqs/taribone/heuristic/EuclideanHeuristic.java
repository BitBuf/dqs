package dev.dewy.dqs.taribone.heuristic;

import dev.dewy.dqs.utils.vector.Vector3d;

public class EuclideanHeuristic implements Heuristic
{
    @Override
    public double calculateCost(Vector3d from, Vector3d to)
    {
        return Math.round(from.distance(to));
    }
}
