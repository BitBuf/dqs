package dev.dewy.dqs.protocol.packet.ingame.server.entity.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerPlayerSetExperiencePacket extends MinecraftPacket
{
    private float experience;
    private int level;
    private int totalExperience;

    @SuppressWarnings("unused")
    private ServerPlayerSetExperiencePacket()
    {
    }

    public ServerPlayerSetExperiencePacket(float experience, int level, int totalExperience)
    {
        this.experience = experience;
        this.level = level;
        this.totalExperience = totalExperience;
    }

    public float getSlot()
    {
        return this.experience;
    }

    public int getLevel()
    {
        return this.level;
    }

    public int getTotalExperience()
    {
        return this.totalExperience;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.experience = in.readFloat();
        this.level = in.readVarInt();
        this.totalExperience = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeFloat(this.experience);
        out.writeVarInt(this.level);
        out.writeVarInt(this.totalExperience);
    }
}
