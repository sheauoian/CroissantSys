package com.github.sheauoian.croissantsys.user.ui

import com.github.sheauoian.croissantsys.user.UserDataOnline
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class StatusGui(private val user: UserDataOnline): ChestGui(6, ComponentHolder.of(Component.text("ステータス"))) {
    init {
        setOnGlobalClick {event -> event.isCancelled = true}

        val background = OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST)
        background.addItem(GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
        background.setRepeat(true)
        addPane(background)

        val equipmentPane = StaticPane(3, 1, 3, 4)

        BodyPart.entries.forEach {
            val item = user.wearing[it]?.item ?: it.empty()
            equipmentPane.addItem(GuiItem(item) { event ->
                event.whoClicked.sendMessage(it.name) },
                it.x, it.y)
        }

        addPane(equipmentPane)
        update()
    }
}