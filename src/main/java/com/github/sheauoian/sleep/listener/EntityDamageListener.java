package com.github.sheauoian.sleep.listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity victim = e.getEntity();
        // Attacker が存在
        if (e instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) e).getDamager();
            // Attacker が生物
            if (attacker instanceof LivingEntity) {
                // Attacker がプレイヤー
                if (attacker instanceof Player) {
                    // Victim が Player -> キャンセル
                    if (victim instanceof Player) {
                        // TODO 特定の環境下でPvPが有効かの確認
                        e.setCancelled(true);
                    }
                    else if (victim instanceof LivingEntity) {
                        e.setDamage(100);
                    }
                }
            }
        } else if (e instanceof EntityDamageByBlockEvent) {
            e.setCancelled(true);
        }
    }
}
