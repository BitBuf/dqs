package dev.dewy.dqs.protocol.game;

import dev.dewy.dqs.protocol.packet.ingame.server.ServerChatPacket;
import dev.dewy.dqs.protocol.profiles.GameProfile;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;
import dev.dewy.dqs.utils.ObjectUtil;

import java.util.Objects;

public class PlayerListEntry
{
    private final GameProfile profile;

    private GameMode gameMode;
    private int ping;
    private String displayName;

    public PlayerListEntry(GameProfile profile, GameMode gameMode, int ping, String displayName, boolean escape)
    {
        this.profile = profile;
        this.gameMode = gameMode;
        this.ping = ping;
        this.displayName = escape ? ServerChatPacket.escapeText(displayName) : displayName;
    }

    public PlayerListEntry(GameProfile profile, GameMode gameMode)
    {
        this.profile = profile;
        this.gameMode = gameMode;
    }

    public PlayerListEntry(GameProfile profile, int ping)
    {
        this.profile = profile;
        this.ping = ping;
    }

    public PlayerListEntry(GameProfile profile, String displayName, boolean escape)
    {
        this.profile = profile;
        this.displayName = escape ? ServerChatPacket.escapeText(displayName) : displayName;
    }

    public PlayerListEntry(GameProfile profile)
    {
        this.profile = profile;
    }

    public GameProfile getProfile()
    {
        return this.profile;
    }

    public GameMode getGameMode()
    {
        return this.gameMode;
    }

    public int getPing()
    {
        return this.ping;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof PlayerListEntry)) return false;

        PlayerListEntry that = (PlayerListEntry) o;
        return Objects.equals(this.profile, that.profile) &&
                this.gameMode == that.gameMode &&
                this.ping == that.ping &&
                Objects.equals(this.displayName, that.displayName);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.profile, this.gameMode, this.ping, this.displayName);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
