package dev.dewy.dqs.taribone.pathing;

import dev.dewy.dqs.utils.vector.Vector3i;

public class BlockPathNode extends PathNode
{
    public BlockPathNode(PathSearch source, Vector3i location, PathNode previous, PathNode next)
    {
        super(location, previous, next);
    }

    public BlockPathNode(Vector3i location)
    {
        super(location);
    }
}
