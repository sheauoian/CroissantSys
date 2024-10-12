package com.github.sheauoian.croissantsys.pve.equipment

import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.util.status.Status
import com.github.sheauoian.croissantsys.util.status.StatusType
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack

class Equipment(
    val id: Int,
    val data: EquipmentData,
    var level: Int,
    val rarity: Int,
    var subStatus: List<Status>
) : DbDriver() {
    constructor(id: Int, data: EquipmentData, level: Int, rarity: Int, subStatusStr: String):
            this(id, data, level, rarity, Json.decodeFromString<List<Status>>(subStatusStr))


    fun getStatus(): Map<StatusType, Double> {
        val map: MutableMap<StatusType, Double> = mutableMapOf()
        map[data.mainStatus.type] = data.mainStatus.getVolumeFromLevel(level)
        subStatus.forEach {
            if (map[it.type] != null)
                map[it.type] = map[it.type]!! + it.volume
            else
                map[it.type] = it.volume
        }
        return map
    }


    fun save() {
        EquipmentManager.instance.save(this)
    }

    val item: ItemStack
        get() {
            val item = data.item
            val meta = item.itemMeta
            val name = meta.displayName() ?: data.name
            if (level >= 1)
                meta.displayName(name.append(MiniMessage.miniMessage().deserialize("<color:#cfaadd> +$level")))
            val lore: MutableList<Component> = listOf(
                Component.text("${data.mainStatus.type.displayName} : ${data.mainStatus.getVolumeFromLevel(level)}"),
                Component.text("rarity: $rarity")
            ).toMutableList()
            lore.add(MiniMessage.miniMessage()
                .deserialize("<color:#bbbbaa>[ ${data.bodyPart.name} ]")
                .decoration(TextDecoration.ITALIC, false))
            lore.add(MiniMessage.miniMessage()
                .deserialize("<color:#bbbbaa>ID: $id")
                .decoration(TextDecoration.ITALIC, false))
            subStatus.forEach {
                lore.add(Component.text(
                    String.format("[sub] ${it.type}, %.2f", it.volume))
                )
            }
            meta.lore(lore)
            item.setItemMeta(meta)
            return item
        }

    fun levelUp() {
        level += 1
    }

    fun addSubStatus(status: Status) {
        val list = subStatus.toMutableList()
        list.add(status)
        subStatus = list
    }

    fun getComponent(): Component {
        return data.name.append(Component.text(toString()))
    }

    override fun toString(): String {
        return "Equipment{UniqueId: ${id}, EquipmentId: ${data.id}}"
    }
}