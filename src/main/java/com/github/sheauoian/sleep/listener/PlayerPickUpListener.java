package com.github.sheauoian.sleep.listener;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.item.StorageItem;
import com.github.sheauoian.sleep.player.SleepPlayer;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerPickUpListener implements Listener {
    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        NBTItem nbtItem = new NBTItem(item);
        if (e.getEntity() instanceof Player player) {
            boolean hasTag = nbtItem.hasTag("sleep_item");
            player.sendMessage(Component.text(hasTag));
            if (!hasTag) return;
            String item_id = new NBTItem(item).getString("sleep_item");
            int amount = item.getAmount();
            SleepPlayer sleepPlayer = Sleep.userManager.get(player);
            SleepItem sleepItem = SleepItemDao.getInstance().getByID(item_id);
            e.setCancelled(true);
            e.getItem().remove();
            if (StorageItemDao.getInstance().add(sleepPlayer, item_id, amount)) {
                player.sendMessage(
                        Component.text("追加でアイテムを入手しました: ")
                                .append(sleepItem.displayName()
                                .append(Component.text(sleepPlayer.storageItem.get(item_id).amount))));
            } else {
                player.sendMessage(
                        Component.text("新しくアイテムを入手しました: ")
                                .append(sleepItem.displayName()));
            }
        }
    }
}
