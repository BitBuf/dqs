package dev.dewy.dqs.taribone.entity.player;

import dev.dewy.dqs.taribone.entity.TariboneLiving;
import dev.dewy.dqs.utils.vector.Vector3d;

import java.util.UUID;

public class TaribonePlayer extends TariboneLiving
{
    public TaribonePlayer(int id, UUID uuid)
    {
        super(id, uuid);
    }

    public Vector3d getEyeLocation()
    {
        return getLocation().add(0, 1.62F, 0);
    }
}
