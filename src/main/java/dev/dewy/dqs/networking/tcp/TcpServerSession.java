package dev.dewy.dqs.networking.tcp;

import dev.dewy.dqs.networking.Server;
import dev.dewy.dqs.packet.PacketProtocol;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class TcpServerSession extends TcpSession
{
    private final Server server;

    public TcpServerSession(String host, int port, PacketProtocol protocol, Server server)
    {
        super(host, port, protocol);
        this.server = server;
    }

    @Override
    public Map<String, Object> getFlags()
    {
        Map<String, Object> ret = super.getFlags();
        ret.putAll(this.server.getGlobalFlags());
        return ret;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);

        this.server.addSession(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);

        this.server.removeSession(this);
    }
}
