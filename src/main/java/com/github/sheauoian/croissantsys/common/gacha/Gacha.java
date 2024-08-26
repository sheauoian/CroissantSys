package com.github.sheauoian.croissantsys.common.gacha;

import com.github.sheauoian.croissantsys.common.user.UserInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class Gacha {
    private final Component title;
    private final GachaType type;

    public Gacha(String title, GachaType type) {
        this(MiniMessage.miniMessage().deserialize(title), type);
    }

    public Gacha(Component title, GachaType type) {
        this.title = title;
        this.type = type;
    }

    public void printData(Player player) {
        player.sendMessage(this.title);
        player.sendMessage(" - ガチャを廻してください");
    }
}