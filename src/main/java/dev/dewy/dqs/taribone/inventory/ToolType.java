package dev.dewy.dqs.taribone.inventory;

public enum ToolType
{
    SWORD(268, 272, 267, 283, 276),
    PICKAXE(270, 274, 257, 285, 278),
    SHOVEL(269, 273, 256, 284, 277),
    AXE(271, 275, 258, 286, 279),
    HOE(290, 291, 292, 294, 293),
    SHEARS(359);

    private final int[] ids;

    ToolType(int... ids)
    {
        this.ids = ids;
    }

    public static ToolType getById(int id)
    {
        for (ToolType type : values())
        {
            for (int typeId : type.ids)
            {
                if (id == typeId)
                {
                    return type;
                }
            }
        }

        return null;
    }

    public int[] getIds()
    {
        return ids.clone();
    }
}
