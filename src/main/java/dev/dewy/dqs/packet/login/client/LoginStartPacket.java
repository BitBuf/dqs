package dev.dewy.dqs.packet.login.client;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;

import java.io.IOException;

public class LoginStartPacket extends MinecraftPacket
{
    private String username;

    @SuppressWarnings("unused")
    private LoginStartPacket()
    {
    }

    public LoginStartPacket(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.username = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.username);
    }

    @Override
    public boolean isPriority()
    {
        return true;
    }
}
