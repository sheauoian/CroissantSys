package com.github.sheauoian.sleep.player.listener;

import com.github.sheauoian.sleep.Sleep;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TextComponent hello = (TextComponent) e.getPlayer().displayName().append(
                Component.text(" Joined the game! おやすみなさい")
        );
        e.joinMessage(hello);
        Sleep.userManager.addOnlinePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Sleep.userManager.remove(e.getPlayer().getUniqueId().toString());
    }
}
