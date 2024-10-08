package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.BaseStatus
import com.github.sheauoian.croissantsys.util.status.StatusType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class EquipmentData(
    val id: String,
    val name: Component,
    val bodyPart: BodyPart,
    private val material: Material,
    val mainStatus: BaseStatus): DbDriver() {
    companion object {
        private var datum: MutableMap<String, EquipmentData> = mutableMapOf()
        fun reload() {
            // Open File
            val file = File(CroissantSys.instance.dataFolder, "equipment_data.yml")
            val conf = YamlConfiguration.loadConfiguration(file)

            datum.clear()
            for (id in conf.getKeys(false)) {
                val name = conf.getComponent("$id.name", JSONComponentSerializer.json()) ?: continue
                val bodyPart = BodyPart.valueOf(conf.getString("$id.body_part") ?: "MainHand")
                val material = Material.valueOf(conf.getString("$id.material") ?: bodyPart.material.name)
                val baseStatusTypeId = conf.getString("$id.main_status.type", "STR")
                val baseStatus = BaseStatus(
                    conf.getDouble("$id.main_status.volume", 10.0),
                    conf.getDouble("$id.main_status.slope", 1.0),
                    StatusType.valueOfOrNull(baseStatusTypeId) ?: StatusType.STR
                )
                datum[id] = EquipmentData(
                    id,
                    name.decoration(TextDecoration.ITALIC, false),
                    bodyPart,
                    material,
                    baseStatus
                    )
            }
            UserData.getLoadedAll().forEach {
                it.reloadWearing()
            }
        }

        fun get(id: String): EquipmentData? {
            return datum[id]
        }

        fun getIds(): List<String> {
            return datum.keys.toList()
        }

        fun getAll(): List<EquipmentData> {
            return datum.values.toList()
        }

        fun save() {
            // Open File
            val file = File(CroissantSys.instance.dataFolder, "equipment_data.yml")
            val conf = YamlConfiguration.loadConfiguration(file)

            datum.values.forEach {
                it.save(conf)
            }

            // Save File
            conf.save(file)
        }

        fun addInitialData(dataId: String, optionalBodyPart: BodyPart?): EquipmentData? {
            if (datum.containsKey(dataId)) return null
            val bodyPart = optionalBodyPart ?: BodyPart.MainHand
            val data = EquipmentData(
                dataId,
                Component.text(dataId),
                bodyPart,
                bodyPart.material,
                BaseStatus(10.0, 1.0, StatusType.STR)
            )
            datum[dataId] = data
            save()
            return data
        }
    }

    val item: ItemStack
        get() {
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.displayName(name)
            item.setItemMeta(meta)
            return item
        }

    fun save(conf: FileConfiguration) {
        conf.setComponent("$id.name", JSONComponentSerializer.json(), name)
        conf.set("$id.body_part", bodyPart.name)
        conf.set("$id.material", material.name)

        conf.set("$id.main_status.volume", mainStatus.volume)
        conf.set("$id.main_status.slope", mainStatus.slope)
        conf.set("$id.main_status.type", mainStatus.type.name)
    }
}