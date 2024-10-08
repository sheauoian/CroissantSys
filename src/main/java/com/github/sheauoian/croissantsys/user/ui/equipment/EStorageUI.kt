package com.github.sheauoian.croissantsys.user.ui.equipment

import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.user.UserDataOnline
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayDeque

class EStorageUI(
    val user: UserDataOnline,
    bodyPart: BodyPart?,
    private val isLevelUpUi: Boolean
): ChestGui(6, ComponentHolder.of(MiniMessage.miniMessage().deserialize(
    "<gradient:#ddcfaa:#cfaadd><b>武器ストレージ</b></gradient>"
))) {
    constructor(user: UserDataOnline, bodyPart: BodyPart?): this(user, bodyPart, false)
    private val collection = user.eManager.getAll(bodyPart)
    private var sortMode = 0
    private var reverse = false

    private val storagePane = PaginatedPane(1, 1, 7, 4)
    private val pagingPane = StaticPane(0, 5, 9, 1)

    init {
        setOnGlobalClick {event -> event.isCancelled = true}

        addPane(storagePane)
        setEquipments()

        pagingPane.addItem(GuiItem(ItemStack(Material.PAPER)) {
            if (storagePane.page >= 1) {
                storagePane.page --
                update()
            }
            user.player.sendMessage("remove")
        }, 0, 0)
        pagingPane.addItem(GuiItem(ItemStack(Material.PAPER)) {
            if (storagePane.page < storagePane.pages - 1) {
                storagePane.page ++
                update()
            }
            user.player.sendMessage("add")
        }, 8, 0)

        pagingPane.addItem(sortButton(0, "入手順にソート"), 4, 0)
        pagingPane.addItem(sortButton(1, "レベル順にソート"), 5, 0)
        pagingPane.addItem(sortButton(2, "装備品ID順にソート"), 6, 0)

        addPane(pagingPane)
        update()
    }

    private fun sortButton(mode: Int, name: String): GuiItem {
        val item = ItemStack(Material.ARROW)
        val meta = item.itemMeta
        meta.displayName(Component.text(name))
        item.setItemMeta(meta)
        return GuiItem(item) {
            if (sortMode == mode) {
                reverse = !reverse
            } else {
                sortMode = mode
                reverse = false
            }
            setEquipments()
            update()
        }
    }

    private fun setEquipments() {
        var sorted = when(sortMode) {
            1 -> collection.sortedBy { -1 * it.level }
            2 -> collection.sortedBy { it.data.id }
            else -> collection
        }
        if (reverse) sorted = sorted.reversed()
        storagePane.clear()
        getItemPane(sorted).forEach {
            storagePane.addPage(it)
        }
    }



    private fun getItemPane(collection: Collection<Equipment>): List<OutlinePane> {
        return getItemPane(ArrayDeque(collection))
    }

    private fun getItemPane(queue: ArrayDeque<Equipment>): List<OutlinePane> {
        val list = ArrayList<OutlinePane>()
        var pane = OutlinePane(0, 0, 7, 4)
        var i = 0
        while (queue.size >= 1) {
            if (i >= 28) {
                list.add(pane)
                pane = OutlinePane(0, 0, 7, 4)
                i = 0
            }
            val e = queue.removeFirst()
            pane.addItem(GuiItem(e.item) { event ->
                if (isLevelUpUi) {
                    user.openELeveling(e)
                } else {
                    when (event.click) {
                        ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> {
                            user.openELeveling(e)
                        }
                        else -> {
                            user.eManager.load(e)
                            user.setWearing(e)
                            user.openStatusMenu()
                        }
                    }
                }
            })
            i ++
        }
        list.add(pane)
        return list
    }
}