package dev.dewy.dqs.cache.data.bossbar;

import dev.dewy.dqs.packet.ingame.server.ServerBossBarPacket;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.cache.CachedData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BossBarCache implements CachedData
{
    protected final Map<UUID, BossBar> cachedBossBars = new ConcurrentHashMap<>();

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        this.cachedBossBars.values().stream().map(BossBar::toMCProtocolLibPacket).forEach(consumer);
    }

    @Override
    public void reset(boolean full)
    {
        this.cachedBossBars.clear();
    }

    @Override
    public String getSendingMessage()
    {
        return String.format("Sending %d boss bars", this.cachedBossBars.size());
    }

    public void add(ServerBossBarPacket packet)
    {
        this.cachedBossBars.put(
                packet.getUUID(),
                new BossBar(packet.getUUID())
                        .setTitle(packet.getTitle())
                        .setHealth(packet.getHealth())
                        .setColor(packet.getColor())
                        .setDivision(packet.getDivision())
                        .setDarkenSky(packet.getDarkenSky())
                        .setDragonBar(packet.isDragonBar())
        );
    }

    public void remove(ServerBossBarPacket packet)
    {
        this.cachedBossBars.remove(packet.getUUID());
    }

    public BossBar get(ServerBossBarPacket packet)
    {
        BossBar bossBar = this.cachedBossBars.get(packet.getUUID());
        if (bossBar == null)
        {
            return new BossBar(packet.getUUID());
        }
        return bossBar;
    }
}
