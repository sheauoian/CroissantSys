package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    private static final Map<OfflinePlayer, UserInfo> USERS = new HashMap<>();
    public void add(OfflinePlayer player) {
        UserInfo info = UserInfoDao.getInstance().insertAndGet(player);
        USERS.put(player, info);
    }
    public UserInfo get(OfflinePlayer player) {
        return USERS.get(player);
    }
    public SleepPlayer get(Player player) {
        return (SleepPlayer) USERS.get(player);
    }
    public Collection<UserInfo> getAll() {
        return USERS.values();
    }

    public void remove(OfflinePlayer player) {
        UserInfo info = USERS.get(player);
        if (info instanceof SleepPlayer sleepPlayer) sleepPlayer.save();
        else UserInfoDao.getInstance().update(info);
        USERS.remove(player);
    }
    public void close() {
        for (UserInfo info : USERS.values()) {
            if (info instanceof SleepPlayer sleepPlayer) sleepPlayer.save();
            else UserInfoDao.getInstance().update(info);
        }
    }
}
