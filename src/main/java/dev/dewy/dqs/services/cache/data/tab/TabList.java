package dev.dewy.dqs.services.cache.data.tab;

import dev.dewy.dqs.protocol.game.PlayerListEntry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static dev.dewy.dqs.utils.Constants.*;

public class TabList
{
    protected final Map<UUID, PlayerEntry> entries = new ConcurrentHashMap<>();
    protected String header = "{\"text\":\"\"}";
    protected String footer = "{\"text\":\"\"}";

    public void add(PlayerListEntry entry)
    {
        CACHE_LOG.debug("Added %s (%s) to tab list", entry.getProfile().getName(), entry.getProfile().getId());

        PlayerEntry coolEntry = PlayerEntry.fromMCProtocolLibEntry(entry);
        this.entries.put(entry.getProfile().getId(), coolEntry);
        WEBSOCKET_SERVER.updatePlayer(coolEntry);
    }

    public void remove(PlayerListEntry entry)
    {
        PlayerEntry removed = this.entries.remove(entry.getProfile().getId());
        if (removed == null && CONFIG.debug.server.cache.unknownplayers)
        {
            CACHE_LOG.error("Could not remove player with UUID: %s", entry.getProfile().getId());
        } else if (removed != null)
        {
            CACHE_LOG.debug("Removed %s (%s) from tab list", removed.name, removed.id);
            WEBSOCKET_SERVER.removePlayer(removed.id);
        }
    }

    public PlayerEntry get(PlayerListEntry entry)
    {
        PlayerEntry e = this.entries.get(entry.getProfile().getId());
        if (e == null)
        {
            if (CONFIG.debug.server.cache.unknownplayers)
            {
                CACHE_LOG.error("Could not find player with UUID: %s", entry.getProfile().getId());
            }
            return new PlayerEntry("", entry.getProfile().getId());
        }
        return e;
    }

    public Collection<PlayerEntry> getEntries()
    {
        return this.entries.values();
    }

    public String getHeader()
    {
        return this.header;
    }

    public TabList setHeader(String header)
    {
        this.header = header;
        return this;
    }

    public String getFooter()
    {
        return this.footer;
    }

    public TabList setFooter(String footer)
    {
        this.footer = footer;
        return this;
    }
}
