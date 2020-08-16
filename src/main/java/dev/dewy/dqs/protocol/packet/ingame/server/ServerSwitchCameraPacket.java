package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerSwitchCameraPacket extends MinecraftPacket
{
    private int cameraEntityId;

    @SuppressWarnings("unused")
    private ServerSwitchCameraPacket()
    {
    }

    public ServerSwitchCameraPacket(int cameraEntityId)
    {
        this.cameraEntityId = cameraEntityId;
    }

    public int getCameraEntityId()
    {
        return this.cameraEntityId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.cameraEntityId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeVarInt(this.cameraEntityId);
    }
}
