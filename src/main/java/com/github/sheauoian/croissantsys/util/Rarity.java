package com.github.sheauoian.croissantsys.util;

public enum Rarity {
    COMMON(2),
    RARE(3),
    EPIC(4),
    LEGENDARY(4);

    private final int statusSlotSize;

    private Rarity(int statusSlotSize) {
        this.statusSlotSize = statusSlotSize;
    }

    public int getStatusSlotSize() {
        return this.statusSlotSize;
    }
}
