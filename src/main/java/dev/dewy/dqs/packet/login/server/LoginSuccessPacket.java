package dev.dewy.dqs.packet.login.server;

import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.profiles.GameProfile;

import java.io.IOException;

public class LoginSuccessPacket extends MinecraftPacket
{
    private GameProfile profile;

    @SuppressWarnings("unused")
    private LoginSuccessPacket()
    {
    }

    public LoginSuccessPacket(GameProfile profile)
    {
        this.profile = profile;
    }

    public GameProfile getProfile()
    {
        return this.profile;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.profile = new GameProfile(in.readString(), in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
    }

    @Override
    public boolean isPriority()
    {
        return true;
    }
}
