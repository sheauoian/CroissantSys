package com.github.sheauoian.sleep.storage;

import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.item.StorageItem;
import com.github.sheauoian.sleep.player.SleepPlayer;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Storage extends Gui {
    private final SleepPlayer sleepPlayer;
    private final PaginationManager pagination = new PaginationManager(this);
    public Storage(SleepPlayer sleepPlayer) {
        super(sleepPlayer.player, "storage:"+sleepPlayer.uuid, Component.text("Storage"), 6);
        this.sleepPlayer = sleepPlayer;
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
                    Math.max(1, StorageItemDao.getInstance().get(
                            sleepPlayer.player.getUniqueId(),
                            loopItem.getId()).amount
                    )).onClick((InventoryClickEvent e) -> {
                        sleepPlayer.player.sendMessage(loopItem.getId());
            }));
        }
    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
        pagination.update();
    }


}
