package com.github.sheauoian.croissantsys.common.magic;

import com.github.sheauoian.croissantsys.common.user.OnlineUser;

import java.util.function.Consumer;

public class Magic {
    private Consumer<OnlineUser> skill;
    public void cast(OnlineUser player) {
        skill.accept(player);
    }
}
