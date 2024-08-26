package com.github.sheauoian.croissantsys.common.storage;

import com.github.sheauoian.croissantsys.common.item.equipment.Equipment;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentDao;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentType;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.configurable.util.XMaterial;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentStorage extends Gui {
    private final Map<EquipmentType, List<Equipment>> storage = new HashMap<>();
    private final PaginationManager pagination = new PaginationManager(this);
    private EquipmentType mode = EquipmentType.HELMET;
    final OnlineUser user;

    public EquipmentStorage(OnlineUser user) {
        super(user.player, "equipment_storage."+user.info.uuid,
                MiniMessage.miniMessage().deserialize("<b>装備ストレージ</b>"), 6);
        this.user = user;
        for (EquipmentType type : EquipmentType.values())
            storage.put(type, new ArrayList<>());
        pagination.registerPageSlotsBetween(0, 44);
        reload();
    }
    @Override
    public void onOpen(InventoryOpenEvent event) {
        resetUi();
        updatePagination();
    }

    public void resetUi() {
        if (pagination.getCurrentPage() != 0) {
            addItem(45, new Icon(XMaterial.ARROW.parseItem()).setName("前のページ").onClick(e -> {
                pagination.goPreviousPage();
                resetUi();
                pagination.update();
            }));
        }
        if (!pagination.isLastPage()) {
            addItem(53, new Icon(XMaterial.ARROW.parseItem()).setName("次のページ").onClick(e -> {
                pagination.goNextPage();
                resetUi();
                pagination.update();
            }));
        }
    }

    private void calculateProducts() {
        pagination.getItems().clear();
        for (Equipment e : storage.get(mode)) {
            ItemStack item = e.getItemStack();
            item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            Icon i = new Icon(item);

            pagination.addItem(i.onClick((InventoryClickEvent event) -> {
                user.info.getEquips().attach(e);
                updatePagination();
            }));
        }
    }

    private void updatePagination() {
        calculateProducts();
        pagination.update();
    }

    public void changeMode(EquipmentType type) {
        mode = type;
    }



    public void addEquipment(Equipment e) {
        if (storage.containsKey(e.getInfo().getType()))
            storage.get(e.getInfo().getType()).add(e);

    }

    public void reload() {
        for (EquipmentType type : EquipmentType.values())
            storage.get(type).clear();
        for (Equipment e : EquipmentDao.getInstance().getAll(user.info.uuid)) {
            addEquipment(e);
        }
    }
}