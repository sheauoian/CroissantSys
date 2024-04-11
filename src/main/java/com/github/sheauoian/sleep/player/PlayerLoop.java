package com.github.sheauoian.sleep.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerLoop extends BukkitRunnable {
    @Override
    public void run() {
        for (UserInfo info : UserManager.getInstance().getAll()) {
            if (!info.player.isOnline()) {
                UserManager.getInstance().remove(info.player);
            }
            info.actionBar();
        }
    }
}
