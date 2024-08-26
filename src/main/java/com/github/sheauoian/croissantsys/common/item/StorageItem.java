package com.github.sheauoian.croissantsys.common.item;

import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageItem extends ServerItem {
    private int amount;
    public StorageItem(ServerItem serverItem, int amount) {
        super(serverItem.getId(), serverItem.getName(), serverItem.getMaterial(), serverItem.getCategory());
        this.amount = amount;
    }

    public StorageItem(String item_id, int amount) {
        this(ServerItem.get(item_id), amount);
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
