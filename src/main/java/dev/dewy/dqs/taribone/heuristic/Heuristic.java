package dev.dewy.dqs.taribone.heuristic;

import dev.dewy.dqs.utils.vector.Vector3d;

public interface Heuristic
{
    double calculateCost(Vector3d from, Vector3d to);
}
