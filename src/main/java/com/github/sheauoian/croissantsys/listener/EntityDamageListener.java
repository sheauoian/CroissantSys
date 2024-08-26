package com.github.sheauoian.croissantsys.listener;

import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentType;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import com.github.sheauoian.croissantsys.common.user.StatusType;
import com.github.sheauoian.croissantsys.common.user.UserInfo;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import com.github.sheauoian.croissantsys.util.Damage;
import com.github.sheauoian.croissantsys.util.DamageSystem;
import com.github.sheauoian.croissantsys.util.Element;
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
                OnlineUser user = UserManager.getInstance().getOnlineUser(player.getUniqueId().toString());
                double damage = e.getDamage();
                boolean isMythicMob = MythicBukkit.inst().getMobManager().isMythicMob(victim);
                boolean isCritical = e_.isCritical();
                boolean isSweep = e_.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK);
                if (isCritical) {
                    damage *= 1.5;
                } else if (isSweep) {
                    damage *= 0.3;
                }
                Element[] element = new Element[0];
                Damage d = new Damage(damage, element);
                Damage.getDamage(d, user.info, null);
                e.setDamage(d.damage);
                attacker.sendMessage(
                        String.format("ダメージをあたえました %s, %s, %s, %s -> %s",
                                user.info.getStatus(StatusType.STRENGTH),
                                isMythicMob,
                                isCritical,
                                isSweep,
                                d.damage
                        )
                );
            }
        }
        if (victim instanceof Player player) {
            OnlineUser user = UserManager.getInstance().getOnlineUser(player.getUniqueId().toString());
            Element[] element = new Element[0];
            Damage d = new Damage(e.getDamage(), element);
            Damage.getDamage(d,null, user.info);
            if (user.damage(d)) {
                e.setDamage(0);
            } else {
                e.setDamage(99999);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        UserManager.getInstance().getOnlineUser(e.getPlayer().getUniqueId().toString()).resetHealth();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        EntityDamageEvent cause = e.getEntity().getLastDamageCause();
        if (cause instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent)cause).getDamager();
            if (attacker instanceof Player p) {
                p.getLocation().getWorld().playSound(
                        p.getLocation(),
                        Sound.AMBIENT_BASALT_DELTAS_ADDITIONS,
                        1.5f,
                        1.5f
                );
                UserManager.getInstance().getOnlineUser(p.getUniqueId().toString()).getXp(100);
            }
        }
    }
}