package dev.dewy.dqs.cache;

import dev.dewy.dqs.cache.data.PlayerCache;
import dev.dewy.dqs.cache.data.ServerProfileCache;
import dev.dewy.dqs.cache.data.bossbar.BossBarCache;
import dev.dewy.dqs.cache.data.chunk.ChunkCache;
import dev.dewy.dqs.cache.data.entity.EntityCache;
import dev.dewy.dqs.cache.data.stats.StatisticsCache;
import dev.dewy.dqs.cache.data.tab.TabListCache;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

import static dev.dewy.dqs.utils.Constants.CACHE_LOG;
import static dev.dewy.dqs.utils.Constants.CONFIG;

public class DataCache
{
    protected static final Collection<Field> dataFields = new ArrayDeque<>();

    static
    {
        try
        {
            for (Field field : DataCache.class.getDeclaredFields())
            {
                field.setAccessible(true);
                if (CachedData.class.isAssignableFrom(field.getType()))
                {
                    if (CONFIG.debug.printDataFields)
                    {
                        CACHE_LOG.debug("Found data field: %s", field.getName());
                    }
                    dataFields.add(field);
                } else if (CONFIG.debug.printDataFields)
                {
                    CACHE_LOG.debug("Class %s is not a valid data field.", field.getType().getCanonicalName());
                }
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        if (CONFIG.debug.printDataFields)
        {
            CACHE_LOG.debug("Found a total of %d data fields.", dataFields.size());
        }
    }

    protected final ThreadLocal<Collection<CachedData>> dataCache = ThreadLocal.withInitial(() -> new ArrayList<>(dataFields.size()));

    protected final ChunkCache chunkCache = new ChunkCache();
    protected final TabListCache tabListCache = new TabListCache();
    protected final BossBarCache bossBarCache = new BossBarCache();
    protected final EntityCache entityCache = new EntityCache();
    protected final PlayerCache playerCache = new PlayerCache();
    protected final ServerProfileCache profileCache = new ServerProfileCache();
    protected final StatisticsCache statsCache = new StatisticsCache();

    public Collection<CachedData> getAllData()
    {
        Collection<CachedData> collection = this.dataCache.get();
        collection.clear();
        dataFields.forEach(field ->
        {
            try
            {
                collection.add((CachedData) field.get(this));
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
        return collection;
    }

    public boolean reset(boolean full)
    {
        CACHE_LOG.debug("Clearing cache...");

        try
        {
            this.getAllData().forEach(d -> d.reset(full));

            CACHE_LOG.debug("Cache cleared.");
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to clear cache", e);
        }
        return true;
    }

    public ThreadLocal<Collection<CachedData>> getDataCache()
    {
        return this.dataCache;
    }

    public ChunkCache getChunkCache()
    {
        return this.chunkCache;
    }

    public TabListCache getTabListCache()
    {
        return this.tabListCache;
    }

    public BossBarCache getBossBarCache()
    {
        return this.bossBarCache;
    }

    public EntityCache getEntityCache()
    {
        return this.entityCache;
    }

    public PlayerCache getPlayerCache()
    {
        return this.playerCache;
    }

    public ServerProfileCache getProfileCache()
    {
        return this.profileCache;
    }

    public StatisticsCache getStatsCache()
    {
        return this.statsCache;
    }
}
