package com.github.sheauoian.sleep.common.magic;

import com.github.sheauoian.sleep.player.OnlineUser;

import java.util.function.Consumer;

public class Magic {
    private Consumer<OnlineUser> skill;
    public void cast(OnlineUser player) {
        skill.accept(player);
    }
}
