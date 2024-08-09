package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.Sleep;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlineUserRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (OnlineUser user: Sleep.userManager.getOnlineUsers()) {
            // if player is offline remove from manager
            if (!user.player.isOnline()) {
                Sleep.userManager.remove(user.info.uuid);
                continue;
            }
            // Check GameMode
            if (user.player.getGameMode().equals(GameMode.SURVIVAL))
                user.player.setGameMode(GameMode.ADVENTURE);

            // show sidebar constantly
            user.sideBar();
        }
    }
}
