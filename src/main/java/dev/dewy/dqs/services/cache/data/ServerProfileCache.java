package dev.dewy.dqs.services.cache.data;

import dev.dewy.dqs.services.cache.CachedData;
import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.profiles.GameProfile;

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
