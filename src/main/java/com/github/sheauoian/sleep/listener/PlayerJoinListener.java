package com.github.sheauoian.sleep.listener;

import com.github.sheauoian.sleep.player.UserManager;
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
        UserManager.getInstance().add(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UserManager.getInstance().remove(e.getPlayer());
    }
}
