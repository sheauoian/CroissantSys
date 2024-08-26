package com.github.sheauoian.croissantsys.common.item.equipment;

public enum EquipmentType {
    HELMET("頭装備"),
    CHEST("胸装備"),
    LEGGINGS("脚装備"),
    BOOTS("靴装備"),
    WEAPON("武器");

    private final String displayName;

    EquipmentType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return this.displayName;
    }
}