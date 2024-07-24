package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    private static final Map<String, UserInfo> LOADED_USERS = new HashMap<>();
    private static final Map<UserInfo, OnlineUser> ONLINE_USERS = new HashMap<>();

    // Online users data
    public void addOnlinePlayer(Player player) {
        UserInfo info = UserInfoDao.getInstance().insertAndGet(player.getUniqueId().toString());
        LOADED_USERS.put(player.getUniqueId().toString(), info);
        ONLINE_USERS.put(info, new OnlineUser(info, player));
    }
    public Collection<OnlineUser> getOnlineUsers() { return ONLINE_USERS.values(); }
    public OnlineUser getOnlineUser(Player p) { return getOnlineUser(p.getUniqueId().toString()); }
    public OnlineUser getOnlineUser(String uuid) { return ONLINE_USERS.get(getInfo(uuid)); }

    // All loaded users data
    public void addUser(String uuid) {
        UserInfo info = UserInfoDao.getInstance().insertAndGet(uuid);
        LOADED_USERS.put(uuid, info);
    }
    public Collection<UserInfo> getLoadedUsers() {
        return LOADED_USERS.values();
    }
    public UserInfo getInfo(String uuid) {
        return LOADED_USERS.get(uuid);
    }

    public void remove(String uuid) {
        UserInfo info = LOADED_USERS.get(uuid);
        if (ONLINE_USERS.containsKey(info)) ONLINE_USERS.get(info).save();
        else UserInfoDao.getInstance().update(info);
        ONLINE_USERS.remove(info);
    }
    public void close() {
        for (UserInfo info : LOADED_USERS.values()) {
            if (ONLINE_USERS.containsKey(info)) ONLINE_USERS.get(info).save();
            else UserInfoDao.getInstance().update(info);
        }
    }
}