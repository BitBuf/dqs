package dev.dewy.dqs.server.handler.postoutgoing;

import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerJoinGamePacket;
import dev.dewy.dqs.packet.ingame.server.ServerPluginMessagePacket;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.utils.RefStrings;

import static dev.dewy.dqs.utils.Constants.*;

public class JoinGamePostHandler implements HandlerRegistry.PostOutgoingHandler<ServerJoinGamePacket, DQSServerConnection>
{
    @Override
    public void accept(ServerJoinGamePacket packet, DQSServerConnection session)
    {
        session.send(new ServerPluginMessagePacket("MC|Brand", RefStrings.BRAND_ENCODED));

        //send cached data
        CACHE.getAllData().forEach(data ->
        {
            if (CONFIG.debug.server.cache.sendingmessages)
            {
                String msg = data.getSendingMessage();
                if (msg == null)
                {
                    SERVER_LOG.debug("Sending data for %s", data.getClass().getCanonicalName());
                } else
                {
                    SERVER_LOG.debug(msg);
                }
            }
            data.getPackets(session::send);
        });

        session.setLoggedIn(true);
    }

    @Override
    public Class<ServerJoinGamePacket> getPacketClass()
    {
        return ServerJoinGamePacket.class;
    }
}
