package com.github.sheauoian.sleep.warppoint;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.player.SleepPlayer;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.configurable.util.XMaterial;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WarpUI extends Gui {
    private final PaginationManager pagination = new PaginationManager(this);
    private final SleepPlayer player;
    public WarpUI(@NotNull SleepPlayer player) {
        super(player.player, "warp_ui:"+player.uuid,
                MiniMessage.miniMessage().deserialize("<gradient:#e63355:#aa8899:#78606f>ワープメニュー</gradient>"),
                5);
        this.player = player;
        pagination.registerPageSlotsBetween(0, 35);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        calculateAndUpdatePagination();
        if (pagination.getCurrentPage() != 0) {
            addItem(36, new Icon(XMaterial.ARROW.parseItem()).setName("§aPrevious").onClick(e -> {
                pagination.goPreviousPage();
                calculateAndUpdatePagination();
            }));
        }
        if (!pagination.isLastPage()) {
            addItem(44, new Icon(XMaterial.ARROW.parseItem()).setName("§aNext").onClick(e -> {
                pagination.goNextPage();
                calculateAndUpdatePagination();
            }));
        }
    }


    private void calculateProducts() {
        pagination.getItems().clear();
        for (WarpPoint point : Sleep.warpPointManager.getAll()) {
            if (UnlockedWarpPointDao.getInstance().has(player.player, point.getId())) {
                pagination.addItem(new Icon(Material.PAPER).setName(point.getName())
                        .appendLore( "§7" +
                                point.getLocation().getBlockX() + ", " +
                                point.getLocation().getBlockY() + ", " +
                                point.getLocation().getBlockZ() + ", " +
                                point.getLocation().getWorld())
                        .onClick((InventoryClickEvent e) -> {
                            point.warp(player.player);
                        }));
            } else {
                pagination.addItem(new Icon(Material.BEDROCK).setName("未開放のワープポイント"));
            }
        }
    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
        pagination.update();
    }
}
