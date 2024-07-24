package com.github.sheauoian.sleep.common.storage;

import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.common.item.SleepItem;
import com.github.sheauoian.sleep.player.OnlineUser;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class Storage extends Gui {
    private final OnlineUser user;
    private final PaginationManager pagination = new PaginationManager(this);
    public Storage(OnlineUser user) {
        super(
                user.player,
                "storage:"+user.info.uuid,
                Component.text("Storage"),
                6);
        this.user = user;
        pagination.registerPageSlotsBetween(0, 44);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        calculateAndUpdatePagination();

        if (pagination.getCurrentPage() != 0) {
            addItem(0, new Icon(Material.PAPER).setName("§aPrevious").onClick(e -> {
                pagination.goPreviousPage();
                calculateAndUpdatePagination();
            }));
        }
    }

    private void calculateProducts() {
        pagination.getItems().clear();
        for (SleepItem loopItem : SleepItemDao.getInstance().getAll()) {
            pagination.addItem(new Icon(loopItem.getItemStack()).setAmount(
                    (int)Math.max(1, StorageItemDao.getInstance().get(
                            user.player.getUniqueId().toString(),
                            loopItem.getId()).amount
                    )).onClick((InventoryClickEvent e) -> {
                        user.player.sendMessage(loopItem.getId());
            }));
        }
    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
        pagination.update();
    }
}
