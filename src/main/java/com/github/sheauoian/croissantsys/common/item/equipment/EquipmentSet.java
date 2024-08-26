package com.github.sheauoian.croissantsys.common.item.equipment;

import com.github.sheauoian.croissantsys.common.user.OnlineUser;

import java.util.function.Consumer;

import static com.github.sheauoian.croissantsys.CroissantSys.logger;

public enum EquipmentSet {
    NORMAL((x) -> {
        logger.info("a");
        logger.info("b");
    });

    private final Consumer<OnlineUser> loop;
    EquipmentSet(Consumer<OnlineUser> c) {
        this.loop = c;
    }
}
