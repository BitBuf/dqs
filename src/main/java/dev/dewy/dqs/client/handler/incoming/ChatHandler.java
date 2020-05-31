package dev.dewy.dqs.client.handler.incoming;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.handler.HandlerRegistry;
import dev.dewy.dqs.packet.ingame.server.ServerChatPacket;
import dev.dewy.dqs.taribone.task.WalkXZTask;
import dev.dewy.dqs.taribone.task.executor.WalkTaskExecutor;
import dev.dewy.dqs.utils.vector.Vector2i;
import dev.dewy.dqs.utils.vector.Vector3i;
import net.daporkchop.lib.minecraft.text.parser.MCFormatParser;

import static dev.dewy.dqs.utils.Constants.*;

public class ChatHandler implements HandlerRegistry.IncomingHandler<ServerChatPacket, DQSClientSession>
{
    @Override
    public boolean apply(ServerChatPacket packet, DQSClientSession session)
    {
        CHAT_LOG.info(packet.getMessage());

        if (MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().contains("walk"))
        {
            WalkXZTask task = new WalkXZTask();
            task.setTarget(new Vector2i(235, 242));

            DQS.getInstance().setTaskExecutor(task.toExecutor(DQS.getInstance()));
        }

        if ("2b2t.org".equals(CONFIG.client.server.address)
                && MCFormatParser.DEFAULT.parse(packet.getMessage()).toRawString().toLowerCase().startsWith("Exception Connecting:".toLowerCase()))
        {
            CLIENT_LOG.error("2b2t's queue is broken as per usual, disconnecting to avoid being stuck forever");
            session.disconnect("§7[§b§lDQS§r§7] §fAntiStuck Disconnect");
        }
        WEBSOCKET_SERVER.fireChat(packet.getMessage());
        return true;
    }

    @Override
    public Class<ServerChatPacket> getPacketClass()
    {
        return ServerChatPacket.class;
    }
}
