package dev.dewy.dqs.cache.data.stats;

import dev.dewy.dqs.cache.CachedData;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.server.ServerAdvancementsPacket;
import dev.dewy.dqs.packet.ingame.server.ServerUnlockRecipesPacket;
import dev.dewy.dqs.protocol.game.advancement.Advancement;
import dev.dewy.dqs.protocol.game.statistic.Statistic;

import java.util.*;
import java.util.function.Consumer;

public class StatisticsCache implements CachedData
{
    protected final Map<Statistic, Integer> statistics = Collections.synchronizedMap(new HashMap<>());

    protected final List<Advancement> advancements = Collections.synchronizedList(new ArrayList<>());
    protected final Map<String, Map<String, Long>> progress = Collections.synchronizedMap(new HashMap<>());
    protected final List<Integer> recipes = Collections.synchronizedList(new ArrayList<>());
    protected final List<Integer> alreadyKnownRecipes = Collections.synchronizedList(new ArrayList<>());
    protected boolean openCraftingBook;
    protected boolean activateFiltering;

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerAdvancementsPacket(
                true,
                this.advancements,
                Collections.emptyList(),
                this.progress
        ));
        consumer.accept(new ServerUnlockRecipesPacket(
                this.openCraftingBook,
                this.activateFiltering,
                this.recipes,
                this.alreadyKnownRecipes
        ));
    }

    @Override
    public void reset(boolean full)
    {
        if (full)
        {
            this.statistics.clear();

            this.advancements.clear();
            this.progress.clear();

            this.openCraftingBook = this.activateFiltering = false;
            this.recipes.clear();
            this.alreadyKnownRecipes.clear();
        }
    }

    public Map<Statistic, Integer> getStatistics()
    {
        return this.statistics;
    }

    public List<Advancement> getAdvancements()
    {
        return this.advancements;
    }

    public Map<String, Map<String, Long>> getProgress()
    {
        return this.progress;
    }

    public boolean isOpenCraftingBook()
    {
        return this.openCraftingBook;
    }

    public StatisticsCache setOpenCraftingBook(boolean openCraftingBook)
    {
        this.openCraftingBook = openCraftingBook;
        return this;
    }

    public boolean isActivateFiltering()
    {
        return this.activateFiltering;
    }

    public StatisticsCache setActivateFiltering(boolean activateFiltering)
    {
        this.activateFiltering = activateFiltering;
        return this;
    }

    public List<Integer> getRecipes()
    {
        return this.recipes;
    }

    public List<Integer> getAlreadyKnownRecipes()
    {
        return this.alreadyKnownRecipes;
    }
}
