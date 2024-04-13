package com.github.sheauoian.sleep.storage;

import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.item.StorageItem;
import com.github.sheauoian.sleep.player.SleepPlayer;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Storage extends Gui {
    private final SleepPlayer sleepPlayer;
    private final PaginationManager pagination = new PaginationManager(this);
    public Storage(SleepPlayer sleepPlayer) {
        super(sleepPlayer.player, "", Component.text("a"), 6);
        this.sleepPlayer = sleepPlayer;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        if (pagination.getCurrentPage() != 0) {
            addItem(0, new Icon(Material.PAPER).setName("§aPrevious").onClick(e -> {
                pagination.goPreviousPage();
                calculateAndUpdatePagination();
            }));
        }
    }

    private void calculateProducts() {
        for (StorageItem item : sleepPlayer.storageItem.values()) {
            pagination.addItem();
        }
    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
    }
}
