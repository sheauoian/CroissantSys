package com.github.sheauoian.sleep.common.item;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SleepItem {
    private final String item_id;
    private final Component item_name;
    private final Material material;
    private final int category;

    public SleepItem(String item_id, String item_name, String material_name, int category) {
        this.item_id = item_id;
        this.item_name = JSONComponentSerializer.json().deserialize(item_name);
        this.material = Material.getMaterial(material_name);
        this.category = category;
    }
    public SleepItem(String item_id, Component item_name, String material_name, int category) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.material = Material.getMaterial(material_name);
        this.category = category;
    }

    public String getId() {
        return this.item_id;
    }


    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(this.item_name);
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("sleep_item", this.item_id);
        return nbtItem.getItem();
    }
    public Component displayName() {
        return this.item_name;
    }

    @Override
    public String toString() {
        return "sleepitem:" + this.item_id;
    }
}
