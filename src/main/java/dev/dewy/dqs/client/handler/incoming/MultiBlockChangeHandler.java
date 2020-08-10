package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.world.ServerMultiBlockChangePacket;
import dev.dewy.dqs.protocol.game.world.block.BlockChangeRecord;

public class MultiBlockChangeHandler implements HandlerRegistry.IncomingHandler<ServerMultiBlockChangePacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerMultiBlockChangePacket packet, DQSClientSession session)
    {
        for (BlockChangeRecord record : packet.getRecords())
        {
            BlockChangeHandler.handleChange(record);
        }
        return true;
    }

    @Override
    public Class<ServerMultiBlockChangePacket> getPacketClass()
    {
        return ServerMultiBlockChangePacket.class;
    }
}
