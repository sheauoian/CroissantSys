package com.github.sheauoian.sleep.common.item;

public enum ItemCategory {
    FOOD(0,true),
    TOOL(0,true),
    MATERIAL(0,true),
    FURNITURE(0,false),
    WEAPON(1,true),
    ARMOR(1,false);

    private final String[] main = {
            "collectable",
            "equipment"
    };

    public final int mainCategory;
    public final boolean isCollective;

    ItemCategory(int mainCategory,
                 boolean isCollective) {
        this.mainCategory = mainCategory;
        this.isCollective = isCollective;
    }

    public String getMain() {
        return main[this.mainCategory];
    }
}

