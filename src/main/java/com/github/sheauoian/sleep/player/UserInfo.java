package com.github.sheauoian.sleep.player;

import com.github.sheauoian.sleep.util.UserLevelUp;

import java.util.UUID;

public class UserInfo {
    public final UUID uuid;
    final String first_login, last_login;
    int level;
    float xp, required_xp, strength, defence, max_health, health;
    public UserInfo(
            UUID uuid,
            String first_login,
            String last_login,
            int level,
            float xp,
            float strength,
            float defence,
            float health,
            float max_health
            )
    {
        this.uuid = uuid;
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
    // オフラインの場合はUserInfoへ経験値の蓄積のみを行う
    public void addXp(float added_xp) {
        this.xp += added_xp;
    }
    public String getFirst_login() {
        return first_login;
    }
    public String getLast_login() {
        return last_login;
    }
    public int getLevel() {
        return this.level;
    }
    public float getXp() {
        return this.xp;
    }
    public double getHealth() {
        return this.health;
    }
    public void resetHealth() {
        this.health = this.max_health;
    }
    // True -> still alive
    // False -> dead
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
