package dev.dewy.dqs.protocol.packet.ingame.client.player;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.PlayerState;

import java.io.IOException;

public class ClientPlayerStatePacket extends MinecraftPacket
{
    private int entityId;
    private PlayerState state;
    private int jumpBoost;

    @SuppressWarnings("unused")
    private ClientPlayerStatePacket()
    {
    }

    public ClientPlayerStatePacket(int entityId, PlayerState state)
    {
        this(entityId, state, 0);
    }

    public ClientPlayerStatePacket(int entityId, PlayerState state, int jumpBoost)
    {
        this.entityId = entityId;
        this.state = state;
        this.jumpBoost = jumpBoost;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public PlayerState getState()
    {
        return this.state;
    }

    public int getJumpBoost()
    {
        return this.jumpBoost;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readVarInt();
        this.state = MagicValues.key(PlayerState.class, in.readVarInt());
        this.jumpBoost = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.entityId);
        out.writeVarInt(MagicValues.value(Integer.class, this.state));
        out.writeVarInt(this.jumpBoost);
    }
}
