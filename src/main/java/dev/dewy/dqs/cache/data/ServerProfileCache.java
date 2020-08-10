package dev.dewy.dqs.cache.data;

import dev.dewy.dqs.cache.CachedData;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.profiles.GameProfile;

import java.util.function.Consumer;

public class ServerProfileCache implements CachedData
{
    protected GameProfile profile;

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
    }

    @Override
    public void reset(boolean full)
    {
        if (full)
        {
            this.profile = null;
        }
    }

    public GameProfile getProfile()
    {
        return this.profile;
    }

    public ServerProfileCache setProfile(GameProfile profile)
    {
        this.profile = profile;
        return this;
    }
}
