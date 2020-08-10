package dev.dewy.dqs.cache.data.tab;

import dev.dewy.dqs.profiles.GameProfile;
import dev.dewy.dqs.protocol.game.PlayerListEntry;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;

import java.util.*;

public class PlayerEntry
{
    protected final String name;
    protected final UUID id;
    protected final Map<GameProfile.TextureType, GameProfile.Texture> textures = new EnumMap<>(GameProfile.TextureType.class);
    protected List<GameProfile.Property> properties = new ArrayList<>();
    protected String displayName;
    protected GameMode gameMode;
    protected int ping;

    public PlayerEntry(String name, UUID id)
    {
        this.name = name;
        this.id = id;
    }

    public static PlayerEntry fromMCProtocolLibEntry(PlayerListEntry in)
    {
        PlayerEntry entry = new PlayerEntry(in.getProfile().getName(), in.getProfile().getId());
        entry.displayName = in.getDisplayName();
        entry.gameMode = in.getGameMode();
        entry.ping = in.getPing();
        entry.textures.putAll(in.getProfile().getTextures());
        entry.properties.addAll(in.getProfile().getProperties());
        return entry;
    }

    public PlayerListEntry toMCProtocolLibEntry()
    {
        PlayerListEntry entry = new PlayerListEntry(
                new GameProfile(this.id, this.name),
                this.gameMode,
                this.ping,
                this.displayName,
                false
        );
        entry.getProfile().getTextures().putAll(this.textures);
        entry.getProfile().getProperties().addAll(this.properties);
        return entry;
    }

    public String getName()
    {
        return this.name;
    }

    public UUID getId()
    {
        return this.id;
    }

    public Map<GameProfile.TextureType, GameProfile.Texture> getTextures()
    {
        return this.textures;
    }

    public List<GameProfile.Property> getProperties()
    {
        return this.properties;
    }

    public PlayerEntry setProperties(List<GameProfile.Property> properties)
    {
        this.properties = properties;
        return this;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public PlayerEntry setDisplayName(String displayName)
    {
        this.displayName = displayName;
        return this;
    }

    public GameMode getGameMode()
    {
        return this.gameMode;
    }

    public PlayerEntry setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
        return this;
    }

    public int getPing()
    {
        return this.ping;
    }

    public PlayerEntry setPing(int ping)
    {
        this.ping = ping;
        return this;
    }
}
