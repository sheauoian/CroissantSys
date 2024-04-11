package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import com.github.sheauoian.sleep.util.UserLevelUp;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class UserInfo {
    public final Player player;

    private final String first_login, last_login;

    private int level;
    private float xp, required_xp, strength, defence, max_health, health;

    public UserInfo(
            Player player,
            String uuid,
            String first_login,
            String last_login,
            int level,
            float xp,
            float strength,
            float defence,
            float health,
            float max_health
            ) {
        this.player = player;
        this.first_login = first_login;
        this.last_login = last_login;
        this.level = level;
        this.xp = xp;
        this.strength = strength;
        this.defence = defence;
        this.health = health;
        this.max_health = max_health;
        this.required_xp = UserLevelUp.getRequiredXp(level);
    }
    public int getLevel() {
        return this.level;
    }
    public float getXp() {
        return this.xp;
    }
    public void addXp(float added_xp) {
        int current_level = level;
        this.xp += added_xp;
        while (this.xp >= required_xp) {
            xp -= required_xp;
            level += 1;
            required_xp = UserLevelUp.getRequiredXp(level);
            player.sendMessage(String.format("Level Up: Lv.%s", level));
        } if (current_level < level) {
            player.sendMessage(String.format("レベルが上がった: %s -> %s", current_level, level));
        } else {
            player.sendMessage(String.format("""
                    現在のレベル: %s
                    必要経験値数: %s
                    入手経験値数: %s
                    蓄積経験値数: %s""",
                    level,
                    required_xp,
                    added_xp,
                    this.xp
                    )
            );
        }
    }



    public void actionBar() {
        player.sendActionBar(Component.text(health + " / " + max_health));
    }
    public void damage(Double damage) {
    }
    public double getHealth() {
        return this.health;
    }
    public void resetHealth() {
        updateHealthBar();
        this.health = this.max_health;
    }
    public void updateHealthBar() {
        player.setHealth(20*health/max_health);
    }
    // True -> still alive
    // False -> dead
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

    public double getMaxHealth() {
        return this.max_health;
    }
    public void setMaxHealth(float max_health) {
        this.max_health = max_health;
    }
    public double getStrength() {
        return this.strength;
    }
    public void setStrength(float strength) {
        if (strength < 1d) strength = 1f;
        this.strength = strength;
    }
    public double getDefence() {
        return this.defence;
    }
    public void setDefence(float defence) {
        if (defence < 1d) defence = 1f;
        this.defence = defence;
    }
}
