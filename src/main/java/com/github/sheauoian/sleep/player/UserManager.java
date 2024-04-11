package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    private static final UserManager singleton = new UserManager();
    private static final Map<Player, UserInfo> USERS = new HashMap<>();
    public void add(Player player) {
        USERS.put(player, UserInfoDao.getInstance().getOnline_ByUUID(player.getUniqueId()));
    }
    public UserInfo get(Player player) {
        return USERS.get(player);
    }
    public Collection<UserInfo> getAll() {
        return USERS.values();
    }
    public void remove(Player player) {
        UserInfoDao.getInstance().update(USERS.get(player));
        USERS.remove(player);
    }
    public void close() {
        for (UserInfo info : USERS.values()) {
            UserInfoDao.getInstance().update(info);
        }
    }

    public static UserManager getInstance() {
        return singleton;
    }
}
