package dev.dewy.dqs.protocol.packet.ingame.client.window;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;

import java.io.IOException;

public class ClientPrepareCraftingGridPacket extends MinecraftPacket
{
    private int windowId;
    private int recipeId;
    private boolean makeAll;

    @SuppressWarnings("unused")
    private ClientPrepareCraftingGridPacket()
    {
    }

    public ClientPrepareCraftingGridPacket(int windowId, int recipeId, boolean makeAll)
    {
        this.windowId = windowId;
        this.recipeId = recipeId;
        this.makeAll = makeAll;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getRecipeId()
    {
        return this.recipeId;
    }

    public boolean doesMakeAll()
    {
        return makeAll;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.windowId = in.readByte();
        this.recipeId = in.readVarInt();
        this.makeAll = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeByte(this.windowId);
        out.writeVarInt(this.recipeId);
        out.writeBoolean(this.makeAll);
    }
}
