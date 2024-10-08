package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.Status
import com.github.sheauoian.croissantsys.util.status.StatusType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack
import java.sql.PreparedStatement

class Equipment(
    val id: Int,
    val data: EquipmentData,
    var level: Int,
    private var subStatus: List<Status>
) : DbDriver() {
    constructor(id: Int, data: EquipmentData, level: Int, subStatusStr: String):
            this(id, data, level, Json.decodeFromString<List<Status>>(subStatusStr))

    companion object {
        // SQL
        private var loadStm: PreparedStatement
        private var insertStm: PreparedStatement
        private var loadUserEquipmentsStm: PreparedStatement
        private var saveStm: PreparedStatement
        init {
            con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS equipments(
                    id              INTEGER     PRIMARY KEY,
                    data_id         STRING      NOT NULL,
                    user_uuid       STRING      NOT NULL,
                    level           INTEGER     DEFAULT 0,
                    sub_status      STRING
                )
            """.trimIndent())

            loadStm = con.prepareStatement("""
                SELECT data_id, level, sub_status FROM equipments WHERE id = ?
            """.trimIndent())

            insertStm = con.prepareStatement("""
                INSERT INTO 
                    equipments  (data_id, user_uuid, sub_status) 
                    VALUES      (?, ?, ?);
                SELECT last_insert_rowid();
            """.trimIndent())

            loadUserEquipmentsStm = con.prepareStatement("""
                SELECT id, data_id, level, sub_status FROM equipments WHERE user_uuid = ?
            """.trimIndent())

            saveStm = con.prepareStatement("""
                UPDATE equipments SET level = ?, sub_status = ? WHERE id = ?
            """.trimIndent())
        }

        fun load(id: Int): Equipment? {
            loadStm.setInt(1, id)
            val rs = loadStm.executeQuery()
            if (rs.next()) {
                val data = EquipmentData.get(rs.getString(1)) ?: return null
                val level = rs.getInt(2)
                val subStatus: List<Status> = try {
                    Json.decodeFromString<List<Status>>(rs.getString(3))
                } catch (e: Exception) {
                    listOf()
                }
                return Equipment(id, data, level, subStatus)
            }
            return null
        }

        fun generate(data: EquipmentData, userUuid: String): Equipment {
            insertStm.setString(1, data.id)
            insertStm.setString(2, userUuid)
            insertStm.setString(3, Json.encodeToString(Status(100.0, StatusType.STR)))
            insertStm.executeUpdate()
            val rs = insertStm.generatedKeys ; rs.next() ; val id = rs.getInt(1)
            return Equipment(id, data, 0, listOf())
        }

        fun loadUserEquipments(uuid: String): List<Equipment> {
            return loadUserEquipments(uuid, null)
        }

        fun loadUserEquipments(uuid: String, bodyPart: BodyPart?): List<Equipment> {
            loadUserEquipmentsStm.setString(1, uuid)
            val rs = loadUserEquipmentsStm.executeQuery()
            val list: MutableList<Equipment> = ArrayList()
            while (rs.next()) {
                if (bodyPart == null ||
                    bodyPart == EquipmentData.get(rs.getString(2))?.bodyPart) {
                    val data = EquipmentData.get(rs.getString(2)) ?: continue
                    list.add(Equipment(
                        rs.getInt(1),
                        data,
                        rs.getInt(3),
                        rs.getString(4) ?: "[]"
                    ))
                }
            }
            return list
        }
    }

    fun save() {
        saveStm.setInt(1, level)
        saveStm.setString(2, Json.encodeToString(subStatus))
        saveStm.setInt(3, id)
        saveStm.execute()
    }

    val item: ItemStack
        get() {
            val item = data.item
            val meta = item.itemMeta
            val name = meta.displayName() ?: data.name
            if (level >= 1)
                meta.displayName(name.append(MiniMessage.miniMessage().deserialize("<color:#cfaadd> +$level")))
            val lore: MutableList<Component> = listOf(
                Component.text("${data.mainStatus.type.displayName} : ${data.mainStatus.getVolumeFromLevel(level)}")
            ).toMutableList()
            lore.add(MiniMessage.miniMessage()
                .deserialize("<color:#bbbbaa>[ ${data.bodyPart.name} ]")
                .decoration(TextDecoration.ITALIC, false))
            lore.add(MiniMessage.miniMessage()
                .deserialize("<color:#bbbbaa>ID: $id")
                .decoration(TextDecoration.ITALIC, false))
            subStatus.forEach {
                lore.add(Component.text("[sub] ${it.type}, ${it.volume}"))
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
        return data.name.append(Component.text(" [unique_id=${id}, level=$level]"))
    }

    override fun toString(): String {
        return "Equipment{UniqueId: ${id}, EquipmentId: ${data.id}}"
    }
}