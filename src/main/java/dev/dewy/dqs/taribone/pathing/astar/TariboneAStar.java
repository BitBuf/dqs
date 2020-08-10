package dev.dewy.dqs.taribone.pathing.astar;

import dev.dewy.dqs.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.taribone.heuristic.Heuristic;
import dev.dewy.dqs.taribone.pathing.BlockPathNode;
import dev.dewy.dqs.taribone.pathing.PathNode;
import dev.dewy.dqs.taribone.world.physics.WorldPhysics;
import dev.dewy.dqs.utils.vector.Vector3i;

import java.util.*;

public class TariboneAStar
{
    private final Heuristic heuristic;
    private final WorldPhysics worldPhysics;

    public TariboneAStar(Heuristic heuristic, WorldPhysics physics)
    {
        this.heuristic = heuristic;
        this.worldPhysics = physics;
    }

    public PathNode search(Vector3i start, Vector3i end) throws ChunkNotLoadedException
    {
        Map<Vector3i, PathNode> nodeMap = new HashMap<>();
        Set<PathNode> visited = new HashSet<>();

        PriorityQueue<PathNode> toVisit = new PriorityQueue<>(Comparator.comparingDouble(thisNode
                -> thisNode.getCost() + heuristic.calculateCost(thisNode.getLocation().doubleVector(), end.doubleVector())
        ));

        PathNode startNode = new PathNode(start);
        startNode.setCost(0);
        nodeMap.put(start, startNode);
        toVisit.add(startNode);

        while (!toVisit.isEmpty() && !Thread.interrupted())
        {
            PathNode current = toVisit.poll();
            visited.add(current);

            if (current.getLocation().distanceSquared(end) <= 1 && worldPhysics.canWalk(current.getLocation(), end))
            {
                PathNode endNode = new PathNode(end, current, null);
                return buildPath(endNode);
            }

            Set<PathNode> neigborNodes = new HashSet<>();
            for (Vector3i vec : worldPhysics.findWalkable(current.getLocation()))
            {
                if (vec == null)
                {
                    continue;
                }

                // Discover neighbour
                if (nodeMap.containsKey(vec))
                {
                    neigborNodes.add(nodeMap.get(vec));
                } else
                {
                    BlockPathNode node = new BlockPathNode(vec);
                    node.setCost(current.getCost() + 1);
                    node.setPrevious(current);
                    neigborNodes.add(node);
                    nodeMap.put(vec, node);
                }
            }

            for (PathNode neighbor : neigborNodes)
            {
                if (visited.contains(neighbor))
                {
                    continue;
                }

                if (!toVisit.contains(neighbor))
                {
                    toVisit.add(neighbor);
                } else if (toVisit.contains(neighbor) && neighbor.getCost() > current.getCost() + 1)
                {
                    toVisit.remove(neighbor);
                    neighbor.setCost(current.getCost() + 1);
                    neighbor.setPrevious(current);
                    toVisit.add(neighbor);
                }
            }
        }
        return null;
    }

    private PathNode buildPath(PathNode end)
    {
        PathNode pointer = end;
        while (pointer.getPrevious() != null)
        {
            pointer.getPrevious().setNext(pointer);
            pointer = pointer.getPrevious();
        }
        return pointer;
    }

    public Heuristic getHeuristic()
    {
        return heuristic;
    }

    public WorldPhysics getWorldPhysics()
    {
        return worldPhysics;
    }
}
