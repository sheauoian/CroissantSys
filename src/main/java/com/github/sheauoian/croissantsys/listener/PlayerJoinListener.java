package com.github.sheauoian.croissantsys.listener;

import com.github.sheauoian.croissantsys.common.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TextComponent hello = (TextComponent) e.getPlayer().displayName().append(
                Component.text(" Joined the game! おやすみなさい")
        );
        e.joinMessage(hello);
        UserManager.getInstance().addOnlinePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UserManager.getInstance().remove(e.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void countDown(final ServerListPingEvent e) {
        String input = "<gradient:#7755BA:#AA5591><bold>CROISSANT WORLD</bold></gradient> ❚ <color:#AAAAAA>Original RPG Server</color>";
        Component comp = MiniMessage.miniMessage().deserialize(input);
        e.motd(comp);
    }
}
