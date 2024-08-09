package com.github.sheauoian.sleep.common.item;

import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import de.tr7zw.nbtapi.NBTItem;
import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SleepItem {
    public static SleepItem get(String item_id) {
        return SleepItemDao.getInstance().getByID(item_id);
    }

    private final String item_id;
    private final Component item_name;
    private final Material material;
    private final ItemCategory category;

    public SleepItem(String item_id, String item_name, String material_name, String category) {
        this(
                item_id,
                JSONComponentSerializer.json().deserialize(item_name),
                Material.getMaterial(material_name),
                ItemCategory.valueOf(category)
        );
    }
    public SleepItem(String item_id, Component item_name, Material material, ItemCategory category) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.material = material;
        this.category = category;
    }

    public String getId() { return this.item_id; }
    public Component getName() { return this.item_name; }
    public Material getMaterial() { return this.material; }
    public ItemCategory getCategory() { return this.category; }

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
        return this.item_id;
    }
}
