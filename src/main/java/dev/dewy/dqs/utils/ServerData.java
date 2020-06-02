package dev.dewy.dqs.utils;

import dev.dewy.dqs.protocol.game.setting.Difficulty;

/**
 * Represents server-related data visible to the Bot.
 */
public class ServerData
{
    private int maxPlayers;
    private Difficulty difficulty;

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty)
    {
        this.difficulty = difficulty;
    }
}
