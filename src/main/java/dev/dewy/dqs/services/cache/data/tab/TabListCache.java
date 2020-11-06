package dev.dewy.dqs.services.cache.data.tab;

import dev.dewy.dqs.services.cache.CachedData;
import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import dev.dewy.dqs.protocol.game.PlayerListEntry;
import dev.dewy.dqs.protocol.game.PlayerListEntryAction;

import java.util.function.Consumer;

public class TabListCache implements CachedData
{
    protected TabList tabList = new TabList();

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerPlayerListDataPacket(this.tabList.getHeader(), this.tabList.getFooter(), false));
        consumer.accept(new ServerPlayerListEntryPacket(
                PlayerListEntryAction.ADD_PLAYER,
                this.tabList.getEntries().stream().map(PlayerEntry::toMCProtocolLibEntry).toArray(PlayerListEntry[]::new)
        ));
    }

    @Override
    public void reset(boolean full)
    {
        if (full)
        {
            this.tabList = new TabList();
        }
    }

    @Override
    public String getSendingMessage()
    {
        return "Sending tab list";
    }

    public TabList getTabList()
    {
        return this.tabList;
    }
}
