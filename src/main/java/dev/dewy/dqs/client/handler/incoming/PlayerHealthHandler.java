package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.client.ClientRequestPacket;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import dev.dewy.dqs.protocol.game.ClientRequest;
import net.daporkchop.lib.common.util.PorkUtil;

import static dev.dewy.dqs.utils.Constants.*;

public class PlayerHealthHandler implements HandlerRegistry.IncomingHandler<ServerPlayerHealthPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerPlayerHealthPacket packet, DQSClientSession session)
    {
        CACHE.getPlayerCache().getThePlayer()
                .setFood(packet.getFood())
                .setSaturation(packet.getSaturation())
                .setHealth(packet.getHealth());
        CACHE_LOG.debug("Player food: %d", packet.getFood())
                .debug("Player saturation: %f", packet.getSaturation())
                .debug("Player health: %f", packet.getHealth());
        if (packet.getHealth() <= 0 && CONFIG.client.extra.autoRespawn.enabled)
        {
            new Thread(() ->
            {
                PorkUtil.sleep(CONFIG.client.extra.autoRespawn.delayMillis);
                if (DQS.getInstance().isConnected() && CACHE.getPlayerCache().getThePlayer().getHealth() <= 0)
                {
                    CACHE.getChunkCache().reset(true); //i don't think this is needed, but it can't hurt
                    DQS.getInstance().getClient().getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
                }
            }).start();
        }
        return true;
    }

    @Override
    public Class<ServerPlayerHealthPacket> getPacketClass()
    {
        return ServerPlayerHealthPacket.class;
    }
}
