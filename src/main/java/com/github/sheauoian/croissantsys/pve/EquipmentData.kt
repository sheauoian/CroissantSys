package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.util.BodyPart
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class EquipmentData(val id: String, val name: Component, val bodyPart: BodyPart, val material: Material) {
    companion object {
        private var datum: MutableMap<String, EquipmentData> = mutableMapOf()
        fun reload() {
            // Open File
            val file = File(CroissantSys.instance.dataFolder, "equipment_data.yml")
            val conf = YamlConfiguration.loadConfiguration(file)

            datum.clear()
            for (id in conf.getKeys(false)) {
                val name = conf.getComponent("$id.name", JSONComponentSerializer.json())
                if (name == null) continue
                val bodyPart = BodyPart.valueOf(conf.getString("$id.body_part") ?: "MainHand")
                val material = Material.valueOf(conf.getString("$id.material") ?: bodyPart.material.name)
                datum.put(id, EquipmentData(id, name, bodyPart, material))
            }
        }

        fun get(id: String): EquipmentData? {
            return datum.get(id)
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

        fun addInitialData(dataId: String): EquipmentData? {
            if (datum.containsKey(dataId)) return null
            val data = EquipmentData(dataId, dataId)
            datum.put(dataId, data)
            save()
            return data
        }
    }


    constructor(id: String, name: String): this(id, Component.text(name), BodyPart.MainHand, BodyPart.MainHand.material)
    constructor(id: String, name: String, bodyPart: BodyPart): this(id, Component.text(name), bodyPart, bodyPart.material)

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
    }
}