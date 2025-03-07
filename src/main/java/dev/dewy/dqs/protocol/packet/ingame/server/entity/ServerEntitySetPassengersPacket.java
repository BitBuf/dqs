package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerEntitySetPassengersPacket extends MinecraftPacket
{
    private int entityId;
    private int[] passengerIds;

    @SuppressWarnings("unused")
    private ServerEntitySetPassengersPacket()
    {
    }

    public ServerEntitySetPassengersPacket(int entityId, int... passengerIds)
    {
        this.entityId = entityId;
        this.passengerIds = passengerIds;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int[] getPassengerIds()
    {
        return this.passengerIds;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.passengerIds = new int[in.readVarInt()];
        for (int index = 0; index < this.passengerIds.length; index++)
        {
            this.passengerIds[index] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.passengerIds.length);
        for (int entityId : this.passengerIds)
        {
            out.writeVarInt(entityId);
        }
    }
}
