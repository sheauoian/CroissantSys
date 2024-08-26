package com.github.sheauoian.croissantsys.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SidebarTitle {
    public static Component getSidebarTitle(){
        return MiniMessage.miniMessage().deserialize("<gradient:#26e6fa:#77a899:#4682b4>Sleep RPG</gradient>");
    }
}
