package dev.dewy.dqs.protocol.packet.ingame.server.entity.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerPlayerHealthPacket extends MinecraftPacket
{
    private float health;
    private int food;
    private float saturation;

    @SuppressWarnings("unused")
    private ServerPlayerHealthPacket()
    {
    }

    public ServerPlayerHealthPacket(float health, int food, float saturation)
    {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    public float getHealth()
    {
        return this.health;
    }

    public int getFood()
    {
        return this.food;
    }

    public float getSaturation()
    {
        return this.saturation;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.health = in.readFloat();
        this.food = in.readVarInt();
        this.saturation = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeFloat(this.health);
        out.writeVarInt(this.food);
        out.writeFloat(this.saturation);
    }
}
