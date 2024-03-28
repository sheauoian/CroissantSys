package com.github.sheauoian.sleep.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TextComponent hello = Component.text(String.format("%s joined", e.getPlayer()));
        e.joinMessage(hello);
    }
}
