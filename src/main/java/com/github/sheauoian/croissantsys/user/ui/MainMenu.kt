package com.github.sheauoian.croissantsys.user.ui

import com.github.sheauoian.croissantsys.user.UserDataOnline
import mc.obliviate.inventory.Gui
import mc.obliviate.inventory.Icon
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class MainMenu(private val user: UserDataOnline)
    : Gui(user.player, "main." + user.uuid, MiniMessage.miniMessage()
        .deserialize("<b>メインメニュー"), 6) {


//    override fun onOpen(event: InventoryOpenEvent) {
//        addItem(45, Icon(Material.COMPASS).setName("ワープ").onClick { _: InventoryClickEvent? ->
//            warp.open()
//        })
//
//        addItem(20, Icon(Material.CHEST).setName("ストレージ").onClick { _: InventoryClickEvent? ->
//            storage.open()
//        })
//    }
//
//    fun save() {
//        StorageItemDao.getInstance().update(user.uuid.toString(), storage)
//    }
}
