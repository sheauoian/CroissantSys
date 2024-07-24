package com.github.sheauoian.sleep.common.item;

import com.github.sheauoian.sleep.util.Rarity;
import com.github.sheauoian.sleep.util.StatusHolder;

public class Equipment {
    private final int id;
    private final String uuid;
    private final String item_id;
    private final Rarity rarity;
    private int level;
    public Equipment(int id, String uuid, String item_id, int rarityLvl, int level) {
        this.id = id;
        this.uuid = uuid;
        this.item_id = item_id;
        this.rarity = Rarity.values()[rarityLvl];
        this.level = level;
    }
    public int getLevel() {return level;}
    public void levelUp(int l) {
        level += l;
    }
    public String getUuid() {return uuid;}
    public String getItemId() {return item_id;}
    public Rarity getRarity() {return rarity;}
}
