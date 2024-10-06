package com.github.sheauoian.croissantsys.item

import de.tr7zw.nbtapi.NBTItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CroissantItem(private val itemId: String, private val itemName: Component, private val material: Material, private val category: CroissantItemCategory) {
    companion object {
        fun get(itemId: String): CroissantItem {
            return CroissantItem(itemId, "TODO", Material.EGG, CroissantItemCategory.FOOD)
        }
    }
    constructor(itemId: String, itemName: String, material: Material, category: CroissantItemCategory) :
            this (
                itemId,
                JSONComponentSerializer.json().deserialize(itemName),
                material,
                category
            )

    fun getItemStack(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName(itemName)
        item.setItemMeta(meta)
        val nbtItem = NBTItem(item)
        nbtItem.setString("CroissantItem", this.itemId)
        return nbtItem.item
    }

    override fun toString(): String {
        return itemId
    }
}