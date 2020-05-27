package dev.dewy.dqs.packet.ingame.client.player;

import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.entity.player.PlayerAction;
import dev.dewy.dqs.protocol.game.world.block.BlockFace;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.networking.NetUtil;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;

import java.io.IOException;

public class ClientPlayerActionPacket extends MinecraftPacket
{
    private PlayerAction action;
    private Position position;
    private BlockFace face;

    @SuppressWarnings("unused")
    private ClientPlayerActionPacket()
    {
    }

    public ClientPlayerActionPacket(PlayerAction action, Position position, BlockFace face)
    {
        this.action = action;
        this.position = position;
        this.face = face;
    }

    public PlayerAction getAction()
    {
        return this.action;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public BlockFace getFace()
    {
        return this.face;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.action = MagicValues.key(PlayerAction.class, in.readVarInt());
        this.position = NetUtil.readPosition(in);
        this.face = MagicValues.key(BlockFace.class, in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        NetUtil.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.face));
    }
}
