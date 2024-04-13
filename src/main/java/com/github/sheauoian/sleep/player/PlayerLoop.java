package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.Sleep;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerLoop extends BukkitRunnable {
    @Override
    public void run() {
        for (UserInfo info : Sleep.userManager.getAll()) {
            if (info instanceof SleepPlayer sleepPlayer) {
                if (!sleepPlayer.player.isOnline()) {
                    Sleep.userManager.remove(sleepPlayer.player);
                    continue;
                }
                sleepPlayer.sideBar();
            }
        }
    }
}
