package dev.dewy.dqs.protocol.packet.ingame.server;

import dev.dewy.dqs.utils.io.NetInput;
import dev.dewy.dqs.utils.io.NetOutput;
import dev.dewy.dqs.protocol.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;
import dev.dewy.dqs.protocol.game.setting.Difficulty;
import dev.dewy.dqs.protocol.game.world.WorldType;

import java.io.IOException;

public class ServerJoinGamePacket extends MinecraftPacket
{
    private int entityId;
    private boolean hardcore;
    private GameMode gamemode;
    private int dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;

    @SuppressWarnings("unused")
    private ServerJoinGamePacket()
    {
    }

    public ServerJoinGamePacket(int entityId, boolean hardcore, GameMode gamemode, int dimension, Difficulty difficulty, int maxPlayers, WorldType worldType, boolean reducedDebugInfo)
    {
        this.entityId = entityId;
        this.hardcore = hardcore;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.worldType = worldType;
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public boolean getHardcore()
    {
        return this.hardcore;
    }

    public GameMode getGameMode()
    {
        return this.gamemode;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public WorldType getWorldType()
    {
        return this.worldType;
    }

    public boolean getReducedDebugInfo()
    {
        return this.reducedDebugInfo;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.entityId = in.readInt();
        int gamemode = in.readUnsignedByte();
        this.hardcore = (gamemode & 8) == 8;
        gamemode &= -9;
        this.gamemode = MagicValues.key(GameMode.class, gamemode);
        this.dimension = in.readInt();
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.maxPlayers = in.readUnsignedByte();
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
        this.reducedDebugInfo = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeInt(this.entityId);
        int gamemode = MagicValues.value(Integer.class, this.gamemode);
        if (this.hardcore)
        {
            gamemode |= 8;
        }

        out.writeByte(gamemode);
        out.writeInt(this.dimension);
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
        out.writeByte(this.maxPlayers);
        out.writeString(MagicValues.value(String.class, this.worldType));
        out.writeBoolean(this.reducedDebugInfo);
    }
}
