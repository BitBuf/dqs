package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.window.ServerSetSlotPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class SetSlotHandler implements HandlerRegistry.IncomingHandler<ServerSetSlotPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerSetSlotPacket packet, DQSClientSession session)
    {
        if (packet.getWindowId() == 0 && packet.getSlot() >= 0)
        {
            CACHE.getPlayerCache().getInventory()[packet.getSlot()] = packet.getItem();
        }
        return true;
    }

    @Override
    public Class<ServerSetSlotPacket> getPacketClass()
    {
        return ServerSetSlotPacket.class;
    }
}
