package dev.dewy.dqs.protocol.packet.ingame.client.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.window.AdvancementTabAction;

import java.io.IOException;

public class ClientAdvancementTabPacket extends MinecraftPacket
{
    private AdvancementTabAction action;
    private String tabId;

    public ClientAdvancementTabPacket()
    {
        this.action = AdvancementTabAction.CLOSED_SCREEN;
    }

    public ClientAdvancementTabPacket(String tabId)
    {
        this.action = AdvancementTabAction.OPENED_TAB;
        this.tabId = tabId;
    }

    public String getTabId()
    {
        if (this.action != AdvancementTabAction.OPENED_TAB)
        {
            throw new IllegalStateException("tabId is only set if action is " + AdvancementTabAction.OPENED_TAB
                    + " but it was " + this.action);
        }
        return tabId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        switch (this.action = MagicValues.key(AdvancementTabAction.class, in.readVarInt()))
        {
            case CLOSED_SCREEN:
                break;
            case OPENED_TAB:
                this.tabId = in.readString();
                break;
            default:
                throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        switch (this.action)
        {
            case CLOSED_SCREEN:
                break;
            case OPENED_TAB:
                out.writeString(this.tabId);
                break;
            default:
                throw new IOException("Unknown advancement tab action: " + this.action);
        }
    }
}
