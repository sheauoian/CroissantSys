package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.api.fastboard.FastBoard;
import com.github.sheauoian.sleep.util.UserLevelUp;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SleepPlayer extends UserInfo {
    public final Player player;
    private final FastBoard board;
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
        this.board = new FastBoard(player);
        //this.board.updateTitle(Component.text("aaaaa"));
        //this.board.updateLine(1, Component.text("hello"));
    }
    public void sideBar() {
        //board.updateLine(1, Component.text("qwerty"));
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
