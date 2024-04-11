package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.api.fastboard.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerLoop extends BukkitRunnable {
    @Override
    public void run() {
        for (UserInfo info : Sleep.userManager.getAll()) {
            if (info instanceof SleepPlayer sleepPlayer) {
                if (!sleepPlayer.player.isOnline()) {
                    Sleep.userManager.remove(sleepPlayer.player);
                    continue;
                }
                //sleepPlayer.actionBar();
                sleepPlayer.sideBar();
            }
        }
    }
}
