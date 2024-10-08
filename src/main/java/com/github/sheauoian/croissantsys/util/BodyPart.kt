package com.github.sheauoian.croissantsys.util

import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BodyPart(val material: Material, val x: Int, val y: Int) {
    MainHand(Material.IRON_SWORD, 0, 1),
    SubHand(Material.SHIELD, 2, 1),
    Head(Material.IRON_HELMET, 1, 0),
    Body(Material.IRON_CHESTPLATE, 1, 1),
    Leg(Material.IRON_LEGGINGS, 1, 2),
    Foot(Material.IRON_BOOTS, 1, 3);

    fun empty(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName(MiniMessage.miniMessage()
            .deserialize("<color:#aaaaaa>[ 空のスロット ]").decoration(TextDecoration.ITALIC, false))
        item.setItemMeta(meta)
        return item
    }
}