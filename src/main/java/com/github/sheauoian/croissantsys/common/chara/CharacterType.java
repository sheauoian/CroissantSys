package com.github.sheauoian.croissantsys.common.chara;

import com.github.sheauoian.croissantsys.util.Rarity;

public enum CharacterType {
    Yoidore(Rarity.EPIC),
    Chelsey(Rarity.EPIC),
    Nial(Rarity.EPIC),
    El_Cobalt(Rarity.EPIC),
    Theta(Rarity.EPIC),
    Shiramo(Rarity.EPIC);

    private final Rarity rarity;


    CharacterType(Rarity rarity) {
        this.rarity = rarity;
    }

    public Rarity getRarity() {
        return rarity;
    }
}