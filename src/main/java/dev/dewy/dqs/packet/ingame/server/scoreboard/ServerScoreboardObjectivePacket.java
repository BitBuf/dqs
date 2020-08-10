package dev.dewy.dqs.packet.ingame.server.scoreboard;

import dev.dewy.dqs.io.NetInput;
import dev.dewy.dqs.io.NetOutput;
import dev.dewy.dqs.packet.MinecraftPacket;
import dev.dewy.dqs.protocol.MagicValues;
import dev.dewy.dqs.protocol.game.scoreboard.ObjectiveAction;
import dev.dewy.dqs.protocol.game.scoreboard.ScoreType;

import java.io.IOException;

public class ServerScoreboardObjectivePacket extends MinecraftPacket
{
    private String name;
    private ObjectiveAction action;
    private String displayName;
    private ScoreType type;

    @SuppressWarnings("unused")
    private ServerScoreboardObjectivePacket()
    {
    }

    public ServerScoreboardObjectivePacket(String name)
    {
        this.name = name;
        this.action = ObjectiveAction.REMOVE;
    }

    public ServerScoreboardObjectivePacket(String name, ObjectiveAction action, String displayName, ScoreType type)
    {
        if (action != ObjectiveAction.ADD && action != ObjectiveAction.UPDATE)
        {
            throw new IllegalArgumentException("(name, action, displayName) constructor only valid for adding and updating objectives.");
        }

        this.name = name;
        this.action = action;
        this.displayName = displayName;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public ObjectiveAction getAction()
    {
        return this.action;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public ScoreType getType()
    {
        return this.type;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        this.name = in.readString();
        this.action = MagicValues.key(ObjectiveAction.class, in.readByte());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE)
        {
            this.displayName = in.readString();
            this.type = MagicValues.key(ScoreType.class, in.readString());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        out.writeString(this.name);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE)
        {
            out.writeString(this.displayName);
            out.writeString(MagicValues.value(String.class, this.type));
        }
    }
}
