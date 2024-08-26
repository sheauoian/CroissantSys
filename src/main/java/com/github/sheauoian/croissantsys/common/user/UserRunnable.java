package com.github.sheauoian.croissantsys.common.user;

import com.github.sheauoian.croissantsys.CroissantSys;
import org.bukkit.scheduler.BukkitRunnable;

public class UserRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (OnlineUser onlineUser: UserManager.getInstance().getOnlineUsers()) onlineUser.update();
    }
}
