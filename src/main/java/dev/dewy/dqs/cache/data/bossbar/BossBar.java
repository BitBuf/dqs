package dev.dewy.dqs.cache.data.bossbar;

import dev.dewy.dqs.packet.ingame.server.ServerBossBarPacket;
import dev.dewy.dqs.protocol.game.BossBarAction;
import dev.dewy.dqs.protocol.game.BossBarColor;
import dev.dewy.dqs.protocol.game.BossBarDivision;

import java.util.UUID;

public class BossBar
{
    protected final UUID uuid;

    protected String title;
    protected float health;
    protected BossBarColor color;
    protected BossBarDivision division;
    protected boolean darkenSky;
    protected boolean dragonBar;

    public BossBar(UUID uuid)
    {
        this.uuid = uuid;
    }

    public ServerBossBarPacket toMCProtocolLibPacket()
    {
        return new ServerBossBarPacket(
                this.uuid,
                BossBarAction.ADD,
                this.title,
                this.health,
                this.color,
                this.division,
                this.darkenSky,
                this.dragonBar,
                false
        );
    }

    public UUID getUuid()
    {
        return this.uuid;
    }

    public String getTitle()
    {
        return this.title;
    }

    public BossBar setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public float getHealth()
    {
        return this.health;
    }

    public BossBar setHealth(float health)
    {
        this.health = health;
        return this;
    }

    public BossBarColor getColor()
    {
        return this.color;
    }

    public BossBar setColor(BossBarColor color)
    {
        this.color = color;
        return this;
    }

    public BossBarDivision getDivision()
    {
        return this.division;
    }

    public BossBar setDivision(BossBarDivision division)
    {
        this.division = division;
        return this;
    }

    public boolean isDarkenSky()
    {
        return this.darkenSky;
    }

    public BossBar setDarkenSky(boolean darkenSky)
    {
        this.darkenSky = darkenSky;
        return this;
    }

    public boolean isDragonBar()
    {
        return this.dragonBar;
    }

    public BossBar setDragonBar(boolean dragonBar)
    {
        this.dragonBar = dragonBar;
        return this;
    }
}
