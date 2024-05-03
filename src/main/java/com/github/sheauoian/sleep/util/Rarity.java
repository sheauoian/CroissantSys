package com.github.sheauoian.sleep.util;

public enum Rarity {
    COMMON(1),
    RARE(2),
    EPIC(3),
    LEGENDARY(4);

    // フィールド
    private final int rarity;

    // コンストラクタ
    private Rarity(int rarity) {
        this.rarity = rarity;
    }
}
