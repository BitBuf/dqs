package dev.dewy.dqs.events.client.handler.incoming;

import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.window.ServerSetSlotPacket;

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
