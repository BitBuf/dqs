package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerAdvancementTabPacket extends MinecraftPacket
{
    private String tabId;

    @SuppressWarnings("unused")
    private ServerAdvancementTabPacket()
    {
    }

    public ServerAdvancementTabPacket(String tabId)
    {
        this.tabId = tabId;
    }

    public String getTabId()
    {
        return this.tabId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        if (in.readBoolean())
        {
            this.tabId = in.readString();
        } else
        {
            this.tabId = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        if (this.tabId != null)
        {
            out.writeBoolean(true);
            out.writeString(this.tabId);
        } else
        {
            out.writeBoolean(false);
        }
    }
}
