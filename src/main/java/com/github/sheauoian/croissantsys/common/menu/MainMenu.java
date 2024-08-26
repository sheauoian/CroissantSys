package com.github.sheauoian.croissantsys.common.menu;

import com.github.sheauoian.croissantsys.common.item.StorageItemDao;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentType;
import com.github.sheauoian.croissantsys.common.storage.EquipmentStorage;
import com.github.sheauoian.croissantsys.common.storage.Storage;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import com.github.sheauoian.croissantsys.common.warppoint.WarpUI;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MainMenu extends Gui {
    private final OnlineUser user;

    public final WarpUI warp;
    public final Storage storage;
    public final EquipmentStorage equipment;

    public MainMenu(OnlineUser user) {
        super(user.player, "main."+user.info.uuid,
                MiniMessage.miniMessage().deserialize("<b>メインメニュー"), 6);
        this.user = user;

        this.warp = new WarpUI(user);
        this.storage = new Storage(user);
        this.equipment = new EquipmentStorage(user);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        addItem(45, new Icon(Material.COMPASS).setName("ワープ").onClick(e -> {
            warp.open();
        }));

        addItem(20, new Icon(Material.CHEST).setName("ストレージ").onClick(e -> {
            storage.open();
        }));

        int s = 4;
        for (EquipmentType type : EquipmentType.values()) {
            addItem(s, new Icon(Material.BEDROCK).setName(type.displayName()).onClick(e -> {
                equipment.changeMode(type);
                equipment.open();
            }));
            s += 9;
        }
    }

    public void save() {
        StorageItemDao.getInstance().update(user.info.uuid, storage);
    }
}
