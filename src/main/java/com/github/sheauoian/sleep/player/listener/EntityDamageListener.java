package com.github.sheauoian.sleep.player.listener;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.player.OnlineUser;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity victim = e.getEntity();
        if (e instanceof EntityDamageByEntityEvent e_) {
            Entity attacker = e_.getDamager();
            if (attacker instanceof Projectile projectile) attacker = (Entity) projectile.getShooter();
            if (victim instanceof Player && attacker instanceof Player) {
                e.setCancelled(true);
                return;
            }
            else if (attacker instanceof Player player) {
                OnlineUser user = Sleep.userManager.getOnlineUser(player.getUniqueId().toString());
                double damage = user.info.getStrength();
                boolean isMythicMob = MythicBukkit.inst().getMobManager().isMythicMob(victim);
                boolean isCritical = e_.isCritical();
                boolean isSweep = e_.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK);
                if (isCritical) {
                    damage *= 1.5;
                } else if (isSweep) {
                    damage *= 0.3;
                }
                e.setDamage(damage);
                attacker.sendMessage(
                        String.format("ダメージをあたえました %s, %s, %s, %s -> %s",
                                user.info.getStrength(),
                                isMythicMob,
                                isCritical,
                                isSweep,
                                damage
                        )
                );
            }
        }
        if (victim instanceof Player player) {
            OnlineUser info = Sleep.userManager.getOnlineUser(player.getUniqueId().toString());
            if (info.damage((float)e.getDamage())) {
                e.setDamage(0);
            } else {
                e.setDamage(99999);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Sleep.userManager.getOnlineUser(e.getPlayer().getUniqueId().toString()).resetHealth();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        EntityDamageEvent cause = e.getEntity().getLastDamageCause();
        if (cause instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent)cause).getDamager();
            if (attacker instanceof Player p) {
                p.sendMessage("たおした!");
                p.getLocation().getWorld().playSound(
                        p.getLocation(),
                        Sound.AMBIENT_BASALT_DELTAS_ADDITIONS,
                        1.5f,
                        1.5f
                );
                Sleep.userManager.getOnlineUser(p.getUniqueId().toString()).getXp(100);
            }
        }
    }
}