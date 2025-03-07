package dev.dewy.dqs.protocol.packet.ingame.client;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.Hand;
import dev.dewy.dqs.protocol.game.setting.ChatVisibility;
import dev.dewy.dqs.protocol.game.setting.SkinPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientSettingsPacket extends MinecraftPacket
{
    private String locale;
    private int renderDistance;
    private ChatVisibility chatVisibility;
    private boolean chatColors;
    private List<SkinPart> visibleParts;
    private Hand mainHand;

    @SuppressWarnings("unused")
    private ClientSettingsPacket()
    {
    }

    public ClientSettingsPacket(String locale, int renderDistance, ChatVisibility chatVisibility, boolean chatColors, SkinPart[] visibleParts, Hand mainHand)
    {
        this.locale = locale;
        this.renderDistance = renderDistance;
        this.chatVisibility = chatVisibility;
        this.chatColors = chatColors;
        this.visibleParts = Arrays.asList(visibleParts);
        this.mainHand = mainHand;
    }

    public String getLocale()
    {
        return this.locale;
    }

    public int getRenderDistance()
    {
        return this.renderDistance;
    }

    public ChatVisibility getChatVisibility()
    {
        return this.chatVisibility;
    }

    public boolean getUseChatColors()
    {
        return this.chatColors;
    }

    public List<SkinPart> getVisibleParts()
    {
        return this.visibleParts;
    }

    public Hand getMainHand()
    {
        return this.mainHand;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.locale = in.readString();
        this.renderDistance = in.readByte();
        this.chatVisibility = MagicValues.key(ChatVisibility.class, in.readVarInt());
        this.chatColors = in.readBoolean();
        this.visibleParts = new ArrayList<SkinPart>();

        int flags = in.readUnsignedByte();
        for (SkinPart part : SkinPart.values())
        {
            int bit = 1 << part.ordinal();
            if ((flags & bit) == bit)
            {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = MagicValues.key(Hand.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.locale);
        out.writeByte(this.renderDistance);
        out.writeVarInt(MagicValues.value(Integer.class, this.chatVisibility));
        out.writeBoolean(this.chatColors);

        int flags = 0;
        for (SkinPart part : this.visibleParts)
        {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);

        out.writeVarInt(MagicValues.value(Integer.class, this.mainHand));
    }
}
