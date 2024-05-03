package com.github.sheauoian.sleep.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SidebarTitle {
    public static Component getSidebarTitle(){
        return MiniMessage.miniMessage().deserialize(
                " <gradient:#e6e6fa:#778899:#4682b4>Sleep RPG</gradient>!"
        );
    }
}
