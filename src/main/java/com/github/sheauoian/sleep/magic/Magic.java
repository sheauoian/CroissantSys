package com.github.sheauoian.sleep.magic;

import com.github.sheauoian.sleep.player.SleepPlayer;

import java.util.function.Consumer;

public class Magic {
    private Consumer<SleepPlayer> skill;
    public void cast(SleepPlayer player) {
        skill.accept(player);
    }
}
