package com.github.sheauoian.sleep.common.item;

import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StorageItem extends SleepItem {
    private int amount;
    public StorageItem(SleepItem sleepItem, int amount) {
        super(sleepItem.getId(), sleepItem.getName(), sleepItem.getMaterial(), sleepItem.getCategory());
        this.amount = amount;
    }

    public StorageItem(String item_id, int amount) {
        this(SleepItem.get(item_id), amount);
    }

    public int getAmount() { return this.amount; }

    public Icon getIcon() {
        ItemStack item = this.getItemStack();
        item.setAmount(Math.max(Math.min(64, this.amount), 1));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("個数: " + this.amount));
        item.lore(lore);

        return new Icon(item);
    }

    public int add(int amount) {
        if (amount <= 0) return 0;
        int max = 9999;
        if (!this.getCategory().isCollective) max = 1;
        if (this.amount + amount >= max) {
            int n = amount - Math.max(0, max - this.amount);
            this.amount = max;
            return n;
        }
        this.amount += amount;
        return 0;
    }
    public int remove(int amount) {
        if (amount <= 0) return 0;
        if (this.amount <= amount) {
            int n = amount - this.amount;
            this.amount = 0;
            return n;
        }
        this.amount -= amount;
        return 0;
    }
    public void set(int amount) {
        this.amount = Math.max(Math.min(9999, amount), 0);
    }
}
