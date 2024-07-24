package com.github.sheauoian.sleep.player.listener;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.common.item.SleepItem;
import com.github.sheauoian.sleep.player.OnlineUser;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

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

            OnlineUser user = Sleep.userManager.getOnlineUser(player);
            SleepItem sleepItem = SleepItemDao.getInstance().getByID(item_id);
            e.setCancelled(true);
            e.getItem().remove();
            if (StorageItemDao.getInstance().add(user, item_id, amount)) {
                player.sendMessage(
                        Component.text("追加でアイテムを入手しました: ")
                                .append(sleepItem.displayName()
                                .append(Component.text(user.storageItem.get(item_id).amount))));
            } else {
                player.sendMessage(
                        Component.text("新しくアイテムを入手しました: ")
                                .append(sleepItem.displayName()));
            }
        }
    }
}
