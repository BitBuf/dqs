package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.setting.Difficulty;

import java.io.IOException;

public class ServerDifficultyPacket extends MinecraftPacket
{
    private Difficulty difficulty;

    @SuppressWarnings("unused")
    private ServerDifficultyPacket()
    {
    }

    public ServerDifficultyPacket(Difficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
    }
}
