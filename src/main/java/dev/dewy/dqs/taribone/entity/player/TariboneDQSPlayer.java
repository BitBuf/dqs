package dev.dewy.dqs.taribone.entity.player;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.protocol.core.Session;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;
import dev.dewy.dqs.utils.Constants;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TariboneDQSPlayer extends TaribonePlayer
{
    private final DQS dqs;
    private final Session session;

    private GameMode gameMode;

    private double walkSpeed;
    private double saturation;

    public TariboneDQSPlayer(DQS dqs, int id)
    {
        super(id, UUID.nameUUIDFromBytes(Constants.CONFIG.authentication.username.getBytes(StandardCharsets.UTF_8)));

        this.dqs = dqs;
        this.session = dqs.getClient().getSession();
    }

    public DQS getDqs()
    {
        return dqs;
    }

    public GameMode getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
    }

    public double getWalkSpeed()
    {
        return walkSpeed;
    }

    public void setWalkSpeed(double walkSpeed)
    {
        this.walkSpeed = walkSpeed;
    }

    public double getSaturation()
    {
        return saturation;
    }

    public void setSaturation(double saturation)
    {
        this.saturation = saturation;
    }
}
