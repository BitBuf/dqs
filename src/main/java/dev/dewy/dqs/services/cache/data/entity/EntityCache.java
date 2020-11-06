package dev.dewy.dqs.services.cache.data.entity;

import dev.dewy.dqs.services.cache.CachedData;
import dev.dewy.dqs.protocol.packet.Packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class EntityCache implements CachedData
{
    protected final Map<Integer, Entity> cachedEntities = new ConcurrentHashMap<>(); //TODO: finish porklib primitive

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        this.cachedEntities.values().forEach(entity -> entity.addPackets(consumer));
    }

    @Override
    public void reset(boolean full)
    {
        if (full)
        {
            this.cachedEntities.clear();
        } else
        {
            this.cachedEntities.keySet().removeIf(i -> i != CACHE.getPlayerCache().getEntityId());
        }
    }

    @Override
    public String getSendingMessage()
    {
        return String.format("Sending %d entities", this.cachedEntities.size());
    }

    public void add(Entity entity)
    {
        this.cachedEntities.put(entity.getEntityId(), entity);
    }

    public void remove(int id)
    {
        this.cachedEntities.remove(id);
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E get(int id)
    {
        return (E) this.cachedEntities.get(id);
    }
}
