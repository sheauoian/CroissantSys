package com.github.sheauoian.sleep.player.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.common.item.SleepItem;
import com.github.sheauoian.sleep.player.OnlineUser;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class PlayerPickUpListener implements Listener {
    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        if (e.getEntity() instanceof Player player) {
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasTag("sleep_item")) return;
            e.setCancelled(true);

            OnlineUser user = Sleep.userManager.getOnlineUser(player);
            String item_id = nbt.getString("sleep_item");
            int amount = item.getAmount();
            int n = user.storage.add(item_id, amount);
            user.message(String.format("&7 [ %d ]", n));
            if (n > 0)
                e.getItem().getItemStack().setAmount(n);
            else
                e.getItem().remove();
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent e) {
        Player p = e.getPlayer();
        Location l = p.getLocation();
        Block b = l.add(0,-1,0).getBlock();
        String id = b.getType().toString();
        if (id.contains("GLAZED_TERRACOTTA") || id.equals("IRON_BLOCK")) {
            l = l.add(0, 4, 0);
            for (int i = 0; i < 30; i++) {
                if (l.getBlock().getType().toString().equals(id)) {
                    p.teleport(l.add(0,1,0));
                    break;
                }
                l = l.add(0, 1, 0);
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player p = e.getPlayer();
        Location l = p.getLocation();
        Block b = l.add(0,-1,0).getBlock();
        String id = b.getType().toString();
        if (id.contains("GLAZED_TERRACOTTA") || id.equals("IRON_BLOCK")) {
            l = l.add(0, -3, 0);
            for (int i = 0; i < 30; i++) {
                if (l.getBlock().getType().toString().equals(id)) {
                    p.teleport(l.add(0,1,0));
                    break;
                }
                l = l.add(0, -1, 0);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        e.setKeepLevel(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Location spawn = Sleep.getInstance().getConfig().getLocation("spawnpoint");
        if (spawn == null) {
            p.sendMessage("スポーン地点が存在しません");
            return;
        }
        e.setRespawnLocation(spawn);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack i = e.getItemDrop().getItemStack();
        e.setCancelled(true);
        Component comp = i.getItemMeta().displayName();
        if (comp == null) comp = i.displayName();
        e.getPlayer().sendMessage(comp);
    }
}
