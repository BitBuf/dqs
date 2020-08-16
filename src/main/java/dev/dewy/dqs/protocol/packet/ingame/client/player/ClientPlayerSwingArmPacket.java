package dev.dewy.dqs.protocol.packet.ingame.client.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.Hand;

import java.io.IOException;

public class ClientPlayerSwingArmPacket extends MinecraftPacket
{
    private Hand hand;

    @SuppressWarnings("unused")
    private ClientPlayerSwingArmPacket()
    {
    }

    public ClientPlayerSwingArmPacket(Hand hand)
    {
        this.hand = hand;
    }

    public Hand getHand()
    {
        return this.hand;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.hand = MagicValues.key(Hand.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(MagicValues.value(Integer.class, this.hand));
    }
}
