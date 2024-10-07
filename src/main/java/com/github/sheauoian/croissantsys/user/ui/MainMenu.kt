package com.github.sheauoian.croissantsys.user.ui

import com.github.sheauoian.croissantsys.user.UserDataOnline
import mc.obliviate.inventory.Gui
import mc.obliviate.inventory.Icon
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class MainMenu(private val user: UserDataOnline): Gui(
    user.player,
    "main." + user.uuid,
    MiniMessage.miniMessage().deserialize("<b>メインメニュー"),
    6
) {
    override fun onOpen(event: InventoryOpenEvent) {
        val sound = Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 1f, 1f)
        player.playSound(sound)
        addItem(45, Icon(Material.COMPASS).setName("ワープ").onClick { _: InventoryClickEvent? ->
            WarpUI(user).open()
        })
        addItem(20, Icon(Material.COMPASS).setName("a").onClick { _: InventoryClickEvent? ->
            StatusGui(user).show(player)
        })
    }
}
