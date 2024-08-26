package com.github.sheauoian.croissantsys.common.user;

import com.github.sheauoian.croissantsys.util.Damage;
import com.github.sheauoian.croissantsys.util.DamageSystem;
import com.github.sheauoian.croissantsys.util.UserLevelUp;

import java.util.HashMap;
import java.util.Map;

public class UserInfo implements DamageSystem {
    private static final double[] initialStatus = {
            1.000,  // 0    STRENGTH
            1.000,  // 1    DEFENCE
            1.000,  // 2    MAX_HP
            1.000,  // 3    HP_HEAL_AMOUNT
            1.000,  // 4    HP_HEAL_COOLDOWN
            1.000,  // 5    CRITICAL_RATE
            1.000,  // 6    CRITICAL_DAMAGE
            1.000,  // 7    MP
            1.000,  // 8    MAX_MP
            1.000,  // 9    MP_HEAL_AMOUNT
            1.000,  // 10   MP_HEAL_COOLDOWN
            1.000,  // 11   WALK_SPEED
            1.000,  // 12
    };

    public final String uuid;
    private final UserEquips userEquips;
    int level;
    float xp, required_xp;
    double health;
    double[] commonStatus;
    public UserInfo(String uuid, int level, float xp, double hp) {
        this.uuid = uuid;
        this.userEquips = new UserEquips(this);
        this.level = level;
        this.xp = xp;
        this.health = hp;
        this.required_xp = UserLevelUp.getRequiredXp(level);
        update();
    }
    public void update() {
        commonStatus = initialStatus.clone();
        userEquips.calcStatus(this);
    }
    public void addStatus(Status s) {
        this.commonStatus[s.type().ordinal()] += s.volume();
    }

    public UserEquips getEquips() {
        return this.userEquips;
    }

    public void addXp(float added_xp) {
        this.xp += added_xp;
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
        this.health = this.commonStatus[2];
    }
    public double getStatus(StatusType type) {
        return this.commonStatus[type.ordinal()];
    }

    @Override
    public void getReceiveDamage(Damage d) {
        // UserInfo
        d.damage /= commonStatus[StatusType.DEFENCE.ordinal()];

        // Equipment
        this.userEquips.getReceiveDamage(d);
    }
    @Override
    public void getInflictDamage(Damage d) {
        // UserInfo
        d.damage *= commonStatus[StatusType.STRENGTH.ordinal()];

        // Equipment
        this.userEquips.getInflictDamage(d);
    }
}
