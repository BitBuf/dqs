package dev.dewy.dqs.server.handler.incoming;

import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.protocol.packet.ingame.client.ClientChatPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.ServerChatPacket;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.utils.Constants;
import net.daporkchop.lib.unsafe.PUnsafe;

public class ServerChatHandler implements HandlerRegistry.IncomingHandler<ClientChatPacket, DQSServerConnection>
{
    protected static final long CLIENTCHATPACKET_MESSAGE_OFFSET = PUnsafe.pork_getOffset(ClientChatPacket.class, "message");

    @Override
    public boolean apply(ClientChatPacket packet, DQSServerConnection session)
    {
        if (packet.getMessage().startsWith(Constants.CONFIG.modules.gameCommands.prefix))
        {
            if ((Constants.CONFIG.modules.gameCommands.prefix + "dc").equalsIgnoreCase(packet.getMessage()))
            {
                session.getDqs().getClient().getSession().disconnect("§7[§b§lDQS§r§7] §fDisconnect command issued.", false);
                return false;
            } else
            {
                session.send(new ServerChatPacket(String.format("§cUnknown command: §o%s", packet.getMessage()), true));
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<ClientChatPacket> getPacketClass()
    {
        return ClientChatPacket.class;
    }
}
