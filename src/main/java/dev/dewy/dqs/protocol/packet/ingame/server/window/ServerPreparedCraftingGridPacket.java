package dev.dewy.dqs.protocol.packet.ingame.server.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ServerPreparedCraftingGridPacket extends MinecraftPacket
{
    private int windowId;
    private int recipeId;

    @SuppressWarnings("unused")
    private ServerPreparedCraftingGridPacket()
    {
    }

    public ServerPreparedCraftingGridPacket(int windowId, int recipeId)
    {
        this.windowId = windowId;
        this.recipeId = recipeId;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getRecipeId()
    {
        return this.recipeId;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.windowId = in.readByte();
        this.recipeId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
        out.writeVarInt(this.recipeId);
    }
}
