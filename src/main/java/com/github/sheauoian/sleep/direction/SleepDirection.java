package com.github.sheauoian.sleep.direction;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.swing.text.html.parser.Entity;

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
