package com.github.sheauoian.croissantsys.common.gacha;

public enum GachaType {
    TUTORIAL(20),
    BASIC(80),
    SPECIAL(80),
    EVENT(50);

    final public int max;

    GachaType(int max) {
        this.max = max;
    }
}
