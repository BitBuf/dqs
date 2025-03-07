package dev.dewy.dqs.protocol.packet.ingame.server.entity;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.attribute.Attribute;
import dev.dewy.dqs.protocol.game.entity.attribute.AttributeModifier;
import dev.dewy.dqs.protocol.game.entity.attribute.AttributeType;
import dev.dewy.dqs.protocol.game.entity.attribute.ModifierOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerEntityPropertiesPacket extends MinecraftPacket
{
    private int entityId;
    private List<Attribute> attributes;

    @SuppressWarnings("unused")
    private ServerEntityPropertiesPacket()
    {
    }

    public ServerEntityPropertiesPacket(int entityId, List<Attribute> attributes)
    {
        this.entityId = entityId;
        this.attributes = attributes;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public List<Attribute> getAttributes()
    {
        return this.attributes;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.attributes = new ArrayList<Attribute>();
        int length = in.readInt();
        for (int index = 0; index < length; index++)
        {
            String key = in.readString();
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
            int len = in.readVarInt();
            for (int ind = 0; ind < len; ind++)
            {
                modifiers.add(new AttributeModifier(in.readUUID(), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
            }

            this.attributes.add(new Attribute(MagicValues.key(AttributeType.class, key), value, modifiers));
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeInt(this.attributes.size());
        for (Attribute attribute : this.attributes)
        {
            out.writeString(MagicValues.value(String.class, attribute.getType()));
            out.writeDouble(attribute.getValue());
            out.writeVarInt(attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers())
            {
                out.writeUUID(modifier.getUUID());
                out.writeDouble(modifier.getAmount());
                out.writeByte(MagicValues.value(Integer.class, modifier.getOperation()));
            }
        }
    }
}
