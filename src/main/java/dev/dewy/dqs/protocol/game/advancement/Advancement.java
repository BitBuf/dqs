package dev.dewy.dqs.protocol.game.advancement;

import dev.dewy.dqs.protocol.game.entity.metadata.ItemStack;
import dev.dewy.dqs.utils.ObjectUtil;

import java.util.List;
import java.util.Objects;

public class Advancement
{
    private final String id;
    private final String parentId;
    private final List<String> criteria;
    private final List<List<String>> requirements;
    private DisplayData displayData;

    public Advancement(String id, String parentId, List<String> criteria, List<List<String>> requirements)
    {
        this.id = id;
        this.parentId = parentId;
        this.criteria = criteria;
        this.requirements = requirements;
    }

    public Advancement(String id, String parentId, List<String> criteria, List<List<String>> requirements, DisplayData displayData)
    {
        this(id, parentId, criteria, requirements);
        this.displayData = displayData;
    }

    public String getId()
    {
        return id;
    }

    public String getParentId()
    {
        return parentId;
    }

    public DisplayData getDisplayData()
    {
        return displayData;
    }

    public List<String> getCriteria()
    {
        return criteria;
    }

    public List<List<String>> getRequirements()
    {
        return requirements;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Advancement)) return false;

        Advancement that = (Advancement) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.parentId, that.parentId) &&
                Objects.equals(this.displayData, that.displayData) &&
                Objects.equals(this.criteria, that.criteria) &&
                Objects.equals(this.requirements, that.requirements);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.parentId, this.displayData, this.criteria, this.requirements);
    }

    @Override
    public String toString()
    {
        return ObjectUtil.toString(this);
    }

    public static class DisplayData
    {
        private final String title;
        private final String description;
        private final ItemStack icon;
        private final FrameType frameType;
        private final boolean showToast;
        private final boolean hidden;
        private final float posX;
        private final float posY;
        private String backgroundTexture;

        public DisplayData(String title, String description, ItemStack icon, FrameType frameType,
                           boolean showToast, boolean hidden, float posX, float posY)
        {
            this.title = title;
            this.description = description;
            this.icon = icon;
            this.frameType = frameType;
            this.showToast = showToast;
            this.hidden = hidden;
            this.posX = posX;
            this.posY = posY;
        }

        public DisplayData(String title, String description, ItemStack icon, FrameType frameType,
                           boolean showToast, boolean hidden, float posX, float posY, String backgroundTexture)
        {
            this(title, description, icon, frameType, showToast, hidden, posX, posY);
            this.backgroundTexture = backgroundTexture;
        }

        public String getTitle()
        {
            return title;
        }

        public String getDescription()
        {
            return description;
        }

        public ItemStack getIcon()
        {
            return icon;
        }

        public FrameType getFrameType()
        {
            return frameType;
        }

        public boolean doesShowToast()
        {
            return showToast;
        }

        public boolean isHidden()
        {
            return hidden;
        }

        public String getBackgroundTexture()
        {
            return backgroundTexture;
        }

        public float getPosX()
        {
            return posX;
        }

        public float getPosY()
        {
            return posY;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof DisplayData)) return false;

            DisplayData that = (DisplayData) o;
            return this.showToast == that.showToast &&
                    this.hidden == that.hidden &&
                    Float.compare(that.posX, this.posX) == 0 &&
                    Float.compare(that.posY, this.posY) == 0 &&
                    Objects.equals(this.title, that.title) &&
                    Objects.equals(this.description, that.description) &&
                    Objects.equals(this.icon, that.icon) &&
                    this.frameType == that.frameType &&
                    Objects.equals(this.backgroundTexture, that.backgroundTexture);
        }

        @Override
        public int hashCode()
        {
            return ObjectUtil.hashCode(this.title, this.description, this.icon, this.frameType, this.showToast, this.hidden, this.backgroundTexture, this.posX, this.posY);
        }

        @Override
        public String toString()
        {
            return ObjectUtil.toString(this);
        }

        public enum FrameType
        {
            TASK,
            CHALLENGE,
            GOAL
        }
    }
}
