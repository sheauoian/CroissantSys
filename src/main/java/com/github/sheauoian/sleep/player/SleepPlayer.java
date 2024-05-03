package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import com.github.sheauoian.sleep.direction.SleepDirection;
import com.github.sheauoian.sleep.item.StorageItem;
import com.github.sheauoian.sleep.storage.Storage;
import com.github.sheauoian.sleep.util.SidebarTitle;
import com.github.sheauoian.sleep.util.UserLevelUp;
import com.github.sheauoian.sleep.warppoint.UnlockedWarpPointDao;
import com.github.sheauoian.sleep.warppoint.WarpPointDao;
import com.github.sheauoian.sleep.warppoint.WarpUI;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SleepPlayer extends UserInfo {
    public final Player player;
    public final Map<String, StorageItem> storageItem;
    public final WarpUI warp_ui;
    public final Storage storage;
    public final FastBoard board;
    public final SleepDirection direction;
    public SleepPlayer
            (
            UUID uuid,
            String first_login,
            String last_login,
            int level,
            float xp,
            float strength,
            float defence,
            float health,
            float max_health,
            Player player
            )
    {
        super(uuid, first_login, last_login, level, xp, strength, defence, health, max_health);
        this.player = player;
        this.storageItem = new HashMap<>();
        this.storage = new Storage(this);
        this.board = new FastBoard(player);
        board.updateTitle(SidebarTitle.getSidebarTitle());
        this.direction = new SleepDirection(player);
        this.warp_ui = new WarpUI(this);
    }

    public void save() {
        UserInfoDao.getInstance().update(this);
        StorageItemDao.getInstance().update(storageItem.values());
    }

    public void sideBar() {
        board.updateLine(1, Component.text(String.format("レベル: %d (残り %f xp)",
                level,
                required_xp - xp
        )));
    }
    public void actionBar() {
        player.sendActionBar(Component.text(health + " / " + max_health));
    }
    public void updateHealthBar() {
        player.setHealth(20*health/max_health);
    }
    public void resetHealth() {
        this.health = this.max_health;
        updateHealthBar();
    }
    public void getXp(float added_xp) {
        int current_level = level;
        xp += added_xp;
        while (xp >= required_xp) {
            xp -= required_xp;
            level += 1;
            required_xp = UserLevelUp.getRequiredXp(level);
        }
        if (current_level < level) {
            player.getWorld().playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    SoundCategory.MASTER,
                    1.7f,
                    1.7f);
            player.sendMessage(String.format(
                    "Level Up: %s -> %s",
                    current_level,
                    level
                    )
            );
        }
    }
    public boolean damage(float damage) {
        damage /= defence;
        // Damage が負の場合は0にする
        if (damage < 0) {
            damage = 0;
        }
        this.health = Math.max(this.health - damage, 0);
        if (this.health <= 0d) {
            player.sendMessage("お前は死んでしまった");
            return false;
        };
        updateHealthBar();
        return true;
    }
}
