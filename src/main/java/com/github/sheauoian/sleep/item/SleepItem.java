package com.github.sheauoian.sleep.item;

import com.github.sheauoian.sleep.Sleep;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SleepItem {
    private String item_id;
    private String item_name;
    private Material material;
    private int category;

    public SleepItem(String item_id, String item_name, String material_name, int category) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.material = Material.getMaterial(material_name);
        this.category = category;
    }
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.PAPER);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("sleep_item", this.item_id);
        return nbtItem.getItem();
    }

    @Override
    public String toString() {
        return "SleepItem;" + this.item_id;
    }

    public void save() {
        Sleep.sleepItemDAO.insert(item_id, item_name, material.toString(), category);
    }
}
