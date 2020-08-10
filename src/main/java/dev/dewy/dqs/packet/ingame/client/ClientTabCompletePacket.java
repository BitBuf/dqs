package dev.dewy.dqs.packet.ingame.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;

import java.io.IOException;

public class ClientTabCompletePacket extends MinecraftPacket
{
    private String text;
    private boolean assumeCommand;
    private Position lookingAt;

    @SuppressWarnings("unused")
    private ClientTabCompletePacket()
    {
    }

    public ClientTabCompletePacket(String text, boolean assumeCommand)
    {
        this(text, assumeCommand, null);
    }

    public ClientTabCompletePacket(String text, boolean assumeCommand, Position lookingAt)
    {
        this.text = text;
        this.assumeCommand = assumeCommand;
        this.lookingAt = lookingAt;
    }

    public String getText()
    {
        return this.text;
    }

    public boolean getAssumeCommand()
    {
        return this.assumeCommand;
    }

    public Position getLookingAt()
    {
        return this.lookingAt;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.text = in.readString();
        this.assumeCommand = in.readBoolean();
        this.lookingAt = in.readBoolean() ? NetUtil.readPosition(in) : null;
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.text);
        out.writeBoolean(this.assumeCommand);
        out.writeBoolean(this.lookingAt != null);
        if (this.lookingAt != null)
        {
            NetUtil.writePosition(out, this.lookingAt);
        }
    }
}
