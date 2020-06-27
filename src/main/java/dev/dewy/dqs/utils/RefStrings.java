package dev.dewy.dqs.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static dev.dewy.dqs.utils.Constants.VERSION;

public final class RefStrings
{
    public static final byte[] BRAND_ENCODED;
    protected static final String BRAND = String.format("vanilla");

    static
    {
        ByteBuf buf = Unpooled.buffer(5 + BRAND.length());
        try
        {
            writeUTF8(buf, BRAND);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        BRAND_ENCODED = buf.array();
    }

    private RefStrings()
    {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    protected static void writeUTF8(ByteBuf buf, String value) throws IOException
    {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= Short.MAX_VALUE)
        {
            throw new IOException("Attempt to write a string with a length greater than Short.MAX_VALUE to ByteBuf!");
        }
        // Write the string's length
        writeVarInt(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    protected static void writeVarInt(ByteBuf buf, int value)
    {
        byte part;
        while (true)
        {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0)
            {
                part |= 0x80;
            }
            buf.writeByte(part);
            if (value == 0)
            {
                break;
            }
        }
    }
}
