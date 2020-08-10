package dev.dewy.dqs.taribone.pathing;

import dev.dewy.dqs.utils.vector.Vector3i;

public class PathNode implements Comparable<PathNode>
{
    private final Vector3i location;
    private PathNode next;
    private PathNode previous;
    private int cost = Integer.MAX_VALUE;

    public PathNode(Vector3i location)
    {
        this.location = location;
    }

    public PathNode(Vector3i location, PathNode previous, PathNode next)
    {
        this.location = location;

        this.previous = previous;
        this.next = next;
    }

    public Vector3i getLocation()
    {
        return location;
    }

    public PathNode getNext()
    {
        return next;
    }

    public void setNext(PathNode next)
    {
        this.next = next;
    }

    public PathNode getPrevious()
    {
        return previous;
    }

    public void setPrevious(PathNode previous)
    {
        this.previous = previous;
    }

    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public boolean isStart()
    {
        return this.previous == null;
    }

    public boolean isEnd()
    {
        return this.next == null;
    }

    @Override
    public int compareTo(PathNode pathNode)
    {
        return this.cost - pathNode.getCost();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        PathNode pathNode = (PathNode) o;

        if (cost != pathNode.cost)
        {
            return false;
        }

        if (next != null ? !next.equals(pathNode.next) : pathNode.next != null)
        {
            return false;
        }

        if (previous != null ? !previous.equals(pathNode.previous) : pathNode.previous != null)
        {
            return false;
        }

        return location.equals(pathNode.location);
    }

    @Override
    public int hashCode()
    {
        return this.location.hashCode();
    }
}
