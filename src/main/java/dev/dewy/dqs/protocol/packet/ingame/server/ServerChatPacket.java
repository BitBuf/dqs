package dev.dewy.dqs.protocol.packet.ingame.server;

import com.google.gson.stream.JsonWriter;
import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.MessageType;

import java.io.IOException;
import java.io.StringWriter;

public class ServerChatPacket extends MinecraftPacket
{
    private String message;
    private MessageType type;

    @SuppressWarnings("unused")
    private ServerChatPacket()
    {
    }

    public ServerChatPacket(String text, boolean escape)
    {
        this(text, MessageType.SYSTEM, escape);
    }

    public ServerChatPacket(String text, MessageType type, boolean escape)
    {
        this.message = escape ? escapeText(text) : text;
        this.type = type;
    }

    public static String escapeText(String message)
    {
        try
        {
            StringWriter writer = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setLenient(true);
            jsonWriter.beginObject().name("text").value(message).endObject().close();
            return writer.toString();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getMessage()
    {
        return this.message;
    }

    public MessageType getType()
    {
        return this.type;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.message = in.readString();
        this.type = MagicValues.key(MessageType.class, in.readByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.message);
        out.writeByte(MagicValues.value(Integer.class, this.type));
    }
}
