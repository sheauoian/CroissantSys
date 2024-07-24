package com.github.sheauoian.sleep.common.direction;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SleepDirection {
    private final Player player;
    public SleepDirection(Player player) {
        this.player = player;
    }
    public void fadeInOut() {
        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 10, true);
        player.addPotionEffect(blind);
    }
}
