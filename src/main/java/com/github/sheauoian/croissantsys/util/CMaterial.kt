package com.github.sheauoian.croissantsys.util

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CMaterial(val material: Material, private val rawName: String) {
    RED(Material.REDSTONE, "&cレッドシュガー"),
    BLUE(Material.LAPIS_LAZULI, "&9オーシャンソルト"),
    GOLD(Material.GOLD_NUGGET, "&6ゴールデンビーンズ");

    fun getItem(): ItemStack {
        val i = ItemStack(material)
        val meta = i.itemMeta
        meta.displayName(
            PlainTextComponentSerializer.plainText().deserialize(rawName)
        )
        val lore: MutableList<Component> = mutableListOf()
        lore.add(
            PlainTextComponentSerializer.plainText().deserialize("&7lore here")
        )
        meta.lore(lore)
        i.setItemMeta(meta)
        return i
    }

    fun getGuiItem(amount: Long): ItemStack {
        val i = getItem()
        if (amount >= 64) i.amount = 64
        else i.amount = 64.coerceAtMost(amount.toInt()).coerceAtLeast(1)
        val meta = i.itemMeta
        val lore: MutableList<Component> = meta.lore() ?: mutableListOf()
        lore.add(
            PlainTextComponentSerializer.plainText().deserialize("&7個数: $amount")
        )
        meta.lore(lore)
        i.setItemMeta(meta)
        return i
    }
}