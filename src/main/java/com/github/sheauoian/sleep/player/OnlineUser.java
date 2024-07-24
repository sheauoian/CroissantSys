package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import com.github.sheauoian.sleep.common.direction.SleepDirection;
import com.github.sheauoian.sleep.common.item.StorageItem;
import com.github.sheauoian.sleep.common.storage.Storage;
import com.github.sheauoian.sleep.util.SidebarTitle;
import com.github.sheauoian.sleep.util.UserLevelUp;
import com.github.sheauoian.sleep.common.warppoint.WarpUI;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class OnlineUser {
    public final UserInfo info;

    public final Player player;
    public final Map<String, StorageItem> storageItem;
    public final WarpUI warp_ui;
    public final Storage storage;
    public final FastBoard board;
    public final SleepDirection direction;

    public OnlineUser(UserInfo info, Player player) {
        this.info = info;
        this.player = player;
        this.storageItem = new HashMap<>();
        this.storage = new Storage(this);
        this.board = new FastBoard(player);
        board.updateTitle(SidebarTitle.getSidebarTitle());
        this.direction = new SleepDirection(player);
        this.warp_ui = new WarpUI(this);
    }

    public void save() {
        UserInfoDao.getInstance().update(this.info);
        StorageItemDao.getInstance().update(storageItem.values());
    }

    public void sideBar() {
        board.updateLine(1, Component.text(String.format("レベル: %d (残り %f xp)",
                info.level,
                info.required_xp - info.xp
        )));
    }
    public void actionBar() {
        player.sendActionBar(Component.text(
                info.health + " / " + info.max_health
            )
        );
    }
    public void updateHealthBar() {
        player.setHealth(20 * info.health/info.max_health);
    }
    public void resetHealth() {
        this.info.health = this.info.max_health;
        updateHealthBar();
    }
    public void getXp(float added_xp) {
        int current_level = info.level;
        info.xp += added_xp;
        while (info.xp >= info.required_xp) {
            info.xp -= info.required_xp;
            info.level += 1;
            info.required_xp = UserLevelUp.getRequiredXp(info.level);
        }
        if (current_level < info.level) {
            player.getWorld().playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    SoundCategory.MASTER,
                    1.7f,
                    1.7f);
            player.sendMessage(String.format(
                            "Level Up: %s -> %s",
                            current_level,
                            info.level
                    )
            );
        }
    }
    public boolean damage(float damage) {
        damage /= info.defence;
        // Damage が負の場合は0にする
        if (damage < 0) {
            damage = 0;
        }
        info.health = Math.max(info.health - damage, 0);
        if (info.health <= 0d) {
            player.sendMessage("お前は死んでしまった");
            return false;
        };
        updateHealthBar();
        return true;
    }
}
