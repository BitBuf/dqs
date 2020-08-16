package dev.dewy.dqs.cache.data;

import dev.dewy.dqs.cache.CachedData;
import dev.dewy.dqs.cache.data.entity.EntityPlayer;
import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import dev.dewy.dqs.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;
import dev.dewy.dqs.protocol.game.entity.player.GameMode;
import dev.dewy.dqs.protocol.game.setting.Difficulty;
import dev.dewy.dqs.protocol.game.world.WorldType;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static dev.dewy.dqs.utils.Constants.CACHE;

public class PlayerCache implements CachedData
{
    protected final ItemStack[] inventory = new ItemStack[46];
    protected boolean hardcore;
    protected boolean reducedDebugInfo;
    protected int maxPlayers;
    protected int dimension;
    protected GameMode gameMode;
    protected WorldType worldType;
    protected Difficulty difficulty;
    protected EntityPlayer thePlayer;

    @Override
    public void getPackets(Consumer<Packet> consumer)
    {
        consumer.accept(new ServerWindowItemsPacket(0, this.inventory.clone()));
        consumer.accept(new ServerPlayerPositionRotationPacket(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), ThreadLocalRandom.current().nextInt(16, 1024)));
    }

    @Override
    public void reset(boolean full)
    {
        if (full)
        {
            this.thePlayer = (EntityPlayer) new EntityPlayer(true).setEntityId(-1);
            this.hardcore = this.reducedDebugInfo = false;
            this.maxPlayers = -1;
            Arrays.fill(this.inventory, null);
        }
        this.dimension = Integer.MAX_VALUE;
        this.gameMode = null;
        this.worldType = null;
        this.difficulty = null;
    }

    @Override
    public String getSendingMessage()
    {
        return String.format(
                "Sending player position: (x=%.2f, y=%.2f, z=%.2f, yaw=%.2f, pitch=%.2f)",
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYaw(),
                this.getPitch()
        );
    }

    public double getX()
    {
        return this.thePlayer.getX();
    }

    public PlayerCache setX(double x)
    {
        this.thePlayer.setX(x);
        return this;
    }

    public double getY()
    {
        return this.thePlayer.getY();
    }

    public PlayerCache setY(double y)
    {
        this.thePlayer.setY(y);
        return this;
    }

    public double getZ()
    {
        return this.thePlayer.getZ();
    }

    public PlayerCache setZ(double z)
    {
        this.thePlayer.setZ(z);
        return this;
    }

    public float getYaw()
    {
        return this.thePlayer.getYaw();
    }

    public PlayerCache setYaw(float yaw)
    {
        this.thePlayer.setYaw(yaw);
        return this;
    }

    public float getPitch()
    {
        return this.thePlayer.getPitch();
    }

    public PlayerCache setPitch(float pitch)
    {
        this.thePlayer.setPitch(pitch);
        return this;
    }

    public int getEntityId()
    {
        return this.thePlayer.getEntityId();
    }

    public PlayerCache setEntityId(int id)
    {
        if (this.thePlayer.getEntityId() != -1)
        {
            CACHE.getEntityCache().remove(this.thePlayer.getEntityId());
        }
        this.thePlayer.setEntityId(id);
        CACHE.getEntityCache().add(this.thePlayer);
        return this;
    }

    public boolean isHardcore()
    {
        return this.hardcore;
    }

    public PlayerCache setHardcore(boolean hardcore)
    {
        this.hardcore = hardcore;
        return this;
    }

    public boolean isReducedDebugInfo()
    {
        return this.reducedDebugInfo;
    }

    public PlayerCache setReducedDebugInfo(boolean reducedDebugInfo)
    {
        this.reducedDebugInfo = reducedDebugInfo;
        return this;
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public PlayerCache setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    public PlayerCache setDimension(int dimension)
    {
        this.dimension = dimension;
        return this;
    }

    public GameMode getGameMode()
    {
        return this.gameMode;
    }

    public PlayerCache setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
        return this;
    }

    public WorldType getWorldType()
    {
        return this.worldType;
    }

    public PlayerCache setWorldType(WorldType worldType)
    {
        this.worldType = worldType;
        return this;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }

    public PlayerCache setDifficulty(Difficulty difficulty)
    {
        this.difficulty = difficulty;
        return this;
    }

    public EntityPlayer getThePlayer()
    {
        return this.thePlayer;
    }

    public PlayerCache setThePlayer(EntityPlayer thePlayer)
    {
        this.thePlayer = thePlayer;
        return this;
    }

    public ItemStack[] getInventory()
    {
        return this.inventory;
    }
}
