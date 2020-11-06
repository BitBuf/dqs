package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SetWindowItemsHandler implements HandlerRegistry.IncomingHandler<ServerWindowItemsPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerWindowItemsPacket packet, DQSClientSession session)
    {
        if (packet.getWindowId() == 0)
        { //player inventory
            ItemStack[] dst = CACHE.getPlayerCache().getInventory();
            System.arraycopy(packet.getItems(), 0, dst, 0, Math.min(dst.length, packet.getItems().length));
        }
        return true;
    }

    @Override
    public Class<ServerWindowItemsPacket> getPacketClass()
    {
        return ServerWindowItemsPacket.class;
    }
}
