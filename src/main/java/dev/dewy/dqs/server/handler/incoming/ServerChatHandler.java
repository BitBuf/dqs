package dev.dewy.dqs.server.handler.incoming;

import dev.dewy.dqs.packet.ingame.client.ClientChatPacket;
import dev.dewy.dqs.packet.ingame.server.ServerChatPacket;
import net.daporkchop.lib.unsafe.PUnsafe;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.handler.HandlerRegistry;

import static dev.dewy.dqs.utils.Constants.SHOULD_RECONNECT;

public class ServerChatHandler implements HandlerRegistry.IncomingHandler<ClientChatPacket, DQSServerConnection>
{
    protected static final long CLIENTCHATPACKET_MESSAGE_OFFSET = PUnsafe.pork_getOffset(ClientChatPacket.class, "message");

    @Override
    public boolean apply(ClientChatPacket packet, DQSServerConnection session)
    {
        if (packet.getMessage().startsWith("!"))
        {
            if (packet.getMessage().startsWith("!!"))
            {
                //allow sending ingame commands to bots or whatever
                PUnsafe.putObject(packet, CLIENTCHATPACKET_MESSAGE_OFFSET, packet.getMessage().substring(1));
                return true;
            } else if ("!dc".equalsIgnoreCase(packet.getMessage()))
            {
                session.getDqs().getClient().getSession().disconnect("User forced disconnect", false);
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
