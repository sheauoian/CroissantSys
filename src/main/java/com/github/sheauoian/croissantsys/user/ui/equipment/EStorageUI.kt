package com.github.sheauoian.croissantsys.user.ui.equipment

import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.user.ui.equipment.pane.EStoragePane
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import io.lumine.mythic.bukkit.utils.menu.ClickAction
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

private val sortKeys: List<String> = listOf(
    "入手順",
    "レベル順",
    "装備品ID順",
    "レアリティ順"
)
private val title = ComponentHolder.of(MiniMessage.miniMessage().deserialize(
    "<gradient:#776233:#623377><b>武器ストレージ</b></gradient>"))


class EStorageUI(val user: UserDataOnline, var bodyPart: BodyPart?, private val isLevelUpUi: Boolean)
    : ChestGui(6, title)
{
    constructor(user: UserDataOnline, bodyPart: BodyPart?): this(user, bodyPart, false)
    private var sortMode = 0
    private var reverse = false

    private val storagePane = EStoragePane(user, bodyPart, isLevelUpUi, 1, 1, 7, 4)
    private val pagingPane = StaticPane(0, 5, 9, 1)

    init {
        setOnGlobalClick {event -> event.isCancelled = true}
        addPane(storagePane)

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
        addPane(pagingPane)
        setButton()
    }

    private fun setButton() {
        pagingPane.addItem(sortButton(), 7, 0)
        pagingPane.addItem(bodyPartButton(), 6, 0)
        update()
    }

    private fun sortItem(name: String): ItemStack {
        val item = ItemStack(Material.ARROW)
        val meta = item.itemMeta
        if (sortMode != 2)
            if (reverse)
                meta.displayName(Component.text("「$name」でソート (昇順)"))
            else
                meta.displayName(Component.text("「$name」でソート (降順)"))
        else
            if (reverse)
                meta.displayName(Component.text("「$name」でソート (辞書順)"))
            else
                meta.displayName(Component.text("「$name」でソート (逆順)"))
        item.setItemMeta(meta)
        return item
    }

    private fun sortButton(): GuiItem {
        return GuiItem(sortItem(sortKeys[sortMode])) {event ->
            if (event.isLeftClick) {
                sortMode = (sortMode + 1) % sortKeys.size
            }
            else if (event.isRightClick) {
                reverse = !reverse
            }
            else return@GuiItem
            storagePane.setEquipments(sortMode, reverse)
            setButton()
        }
    }

    private fun bodyPartItem(): ItemStack {
        val b = bodyPart
        val item: ItemStack
        if (b != null) {
            item = ItemStack(b.material)
        }
        else {
            item = ItemStack(Material.GLASS)
        }
        val meta = item.itemMeta
        meta.displayName(Component.text("パーツによるフィルター"))
        val lore = ArrayList<Component>()
        BodyPart.entries.forEach {
            if (bodyPart == it)
                lore.add(Component.text(it.name).color(TextColor.color(0xdddd66)))
            else
                lore.add(Component.text(it.name).color(TextColor.color(0x989898)))
        }
        meta.lore(lore)
        item.setItemMeta(meta)
        return item
    }

    private fun bodyPartButton(): GuiItem {
        return GuiItem(bodyPartItem()) { event ->
            val b = bodyPart
            if ((event.click == ClickType.DROP || event.click == ClickType.CONTROL_DROP) && b != null) {
                bodyPart = null
                storagePane.reset(null)
                setButton()
                return@GuiItem
            } else if (b == null) {
                bodyPart = BodyPart.MainHand
            } else {
                var bodyPartIdx = b.ordinal
                val size = BodyPart.entries.size
                if (event.isLeftClick) {
                    bodyPartIdx -= 1
                    if (bodyPartIdx < 0)
                        bodyPartIdx = size - 1
                }
                else if (event.isRightClick) {
                    bodyPartIdx += 1
                    if (bodyPartIdx >= size)
                        bodyPartIdx = 0
                } else return@GuiItem
                bodyPart = BodyPart.entries[bodyPartIdx]
            }

            storagePane.reset(bodyPart)
            setButton()
        }
    }
}