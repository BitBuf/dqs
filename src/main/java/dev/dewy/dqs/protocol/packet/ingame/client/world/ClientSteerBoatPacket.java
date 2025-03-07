package dev.dewy.dqs.protocol.packet.ingame.client.world;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ClientSteerBoatPacket extends MinecraftPacket
{
    private boolean rightPaddleTurning;
    private boolean leftPaddleTurning;

    @SuppressWarnings("unused")
    private ClientSteerBoatPacket()
    {
    }

    public ClientSteerBoatPacket(boolean rightPaddleTurning, boolean leftPaddleTurning)
    {
        this.rightPaddleTurning = rightPaddleTurning;
        this.leftPaddleTurning = leftPaddleTurning;
    }

    public boolean isRightPaddleTurning()
    {
        return this.rightPaddleTurning;
    }

    public boolean isLeftPaddleTurning()
    {
        return this.leftPaddleTurning;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.rightPaddleTurning = in.readBoolean();
        this.leftPaddleTurning = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeBoolean(this.rightPaddleTurning);
        out.writeBoolean(this.leftPaddleTurning);
    }
}
