package com.github.sheauoian.croissantsys.common.storage;

import com.github.sheauoian.croissantsys.common.item.ServerItem;
import com.github.sheauoian.croissantsys.common.item.StorageItem;
import com.github.sheauoian.croissantsys.common.item.StorageItemDao;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Storage extends Gui {
    // Gui
    private final PaginationManager pagination = new PaginationManager(this);

    // Storage Data
    private final OnlineUser user;
    private final Map<String, StorageItem> items = new HashMap<>();

    public Storage(OnlineUser user) {
        super(user.player, "storage:"+user.info.uuid, Component.text("Storage"), 6);
        pagination.registerPageSlotsBetween(0, 44);
        this.user = user;
        for (StorageItem i : StorageItemDao.getInstance().getAll(user.info.uuid)) {
            user.player.sendMessage(i.getName());
            user.message(String.format(" - 個数: %d", i.getAmount()));
            items.put(i.getId(), i);
        }
    }

    public Collection<StorageItem> getStorageItems() {
        return items.values();
    }

    public int add(String id, int amount) {
        if (!items.containsKey(id)) {
            user.message("&6≫ &7新しいアイテムを入手しました &f&l「"+id+"」");
            items.put(id, new StorageItem(ServerItem.get(id), 0));
        }
        int n = items.get(id).add(amount);
        user.message(" &e[ + "+(amount-n)+"] &7("+n+")");
        return n;
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
        for (StorageItem i : items.values()) {
            if (i.getAmount() < 1) continue;
            Icon icon = i.getIcon().onClick((InventoryClickEvent e) -> {
                        if (!i.getCategory().isCollective) return;
                        int g = i.remove(64);
                        ItemStack item = i.getItemStack();
                        item.setAmount(64 - g);
                        user.player.getInventory().addItem(item);
                        calculateAndUpdatePagination();
                    });
            pagination.addItem(icon);
        }
    }

    private void calculateAndUpdatePagination() {
        this.calculateProducts();
        pagination.update();
    }
}
