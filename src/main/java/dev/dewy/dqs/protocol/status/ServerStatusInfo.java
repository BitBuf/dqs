package dev.dewy.dqs.protocol.status;

import dev.dewy.dqs.packet.ingame.server.ServerChatPacket;
import dev.dewy.dqs.utils.ObjectUtil;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class ServerStatusInfo
{
    private final VersionInfo version;
    private final PlayerInfo players;
    private final String description;
    private final BufferedImage icon;

    public ServerStatusInfo(VersionInfo version, PlayerInfo players, String description, BufferedImage icon, boolean escape)
    {
        this.version = version;
        this.players = players;
        this.description = escape ? ServerChatPacket.escapeText(description) : description;
        this.icon = icon;
    }

    public VersionInfo getVersionInfo()
    {
        return this.version;
    }

    public PlayerInfo getPlayerInfo()
    {
        return this.players;
    }

    public String getDescription()
    {
        return this.description;
    }

    public BufferedImage getIcon()
    {
        return this.icon;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ServerStatusInfo)) return false;

        ServerStatusInfo that = (ServerStatusInfo) o;
        return Objects.equals(this.version, that.version) &&
                Objects.equals(this.players, that.players) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.icon, that.icon);
    }

    @Override
    public int hashCode()
    {
        return ObjectUtil.hashCode(this.version, this.players, this.description, this.icon);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }
}
