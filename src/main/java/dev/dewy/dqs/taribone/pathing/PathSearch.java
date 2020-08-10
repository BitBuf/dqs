package dev.dewy.dqs.taribone.pathing;

import dev.dewy.dqs.utils.vector.Vector3i;

public interface PathSearch
{
    void step();

    boolean isDone();

    Vector3i getStart();

    Vector3i getEnd();

    PathNode getPath();

    PathSearchProvider getSource();
}