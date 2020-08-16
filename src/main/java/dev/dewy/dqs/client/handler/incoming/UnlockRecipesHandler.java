package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerUnlockRecipesPacket;

import static dev.dewy.dqs.utils.Constants.CACHE;
import static dev.dewy.dqs.utils.Constants.CLIENT_LOG;

public class UnlockRecipesHandler implements HandlerRegistry.IncomingHandler<ServerUnlockRecipesPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerUnlockRecipesPacket packet, DQSClientSession session)
    {
        CACHE.getStatsCache()
                .setActivateFiltering(packet.getActivateFiltering())
                .setOpenCraftingBook(packet.getOpenCraftingBook());

        switch (packet.getAction())
        {
            case INIT:
                CLIENT_LOG.debug("Init recipes: recipes=%d, known=%d", packet.getRecipes().size(), packet.getAlreadyKnownRecipes().size());
                CACHE.getStatsCache().getRecipes().addAll(packet.getRecipes());
                CACHE.getStatsCache().getAlreadyKnownRecipes().addAll(packet.getAlreadyKnownRecipes());
                break;
            case ADD:
                CACHE.getStatsCache().getAlreadyKnownRecipes().addAll(packet.getRecipes());
                break;
            case REMOVE:
                CACHE.getStatsCache().getAlreadyKnownRecipes().removeAll(packet.getRecipes());
                break;
        }

        return true;
    }

    @Override
    public Class<ServerUnlockRecipesPacket> getPacketClass()
    {
        return ServerUnlockRecipesPacket.class;
    }
}
