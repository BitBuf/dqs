package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerPlayerListDataPacket extends MinecraftPacket
{
    private String header;
    private String footer;

    @SuppressWarnings("unused")
    private ServerPlayerListDataPacket()
    {
    }

    public ServerPlayerListDataPacket(String header, String footer, boolean escape)
    {
        this.header = escape ? ServerChatPacket.escapeText(header) : header;
        this.footer = escape ? ServerChatPacket.escapeText(footer) : footer;
    }

    public String getHeader()
    {
        return this.header;
    }

    public String getFooter()
    {
        return this.footer;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.header = in.readString();
        this.footer = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.header);
        out.writeString(this.footer);
    }
}
