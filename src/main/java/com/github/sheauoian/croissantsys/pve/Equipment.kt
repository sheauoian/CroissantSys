package com.github.sheauoian.croissantsys.pve

import com.github.sheauoian.croissantsys.DbDriver
import net.kyori.adventure.text.Component
import java.sql.PreparedStatement
import java.util.*

class Equipment(val uniqueId: String, private val equipmentData: EquipmentData): DbDriver() {
    companion object {
        // SQL
        private var loadStm: PreparedStatement
        private var insertStm: PreparedStatement
        private var loadUserEquipmentsStm: PreparedStatement
        init {
            con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS equipments(
                    id              STRING      PRIMARY KEY,
                    data_id         STRING      NOT NULL,
                    user_uuid       STRING      NOT NULL
                )
            """.trimIndent())

            loadStm = con.prepareStatement("""
                SELECT data_id, user_uuid FROM equipments WHERE id = ?
            """.trimIndent())

            insertStm = con.prepareStatement("""
                INSERT INTO 
                    equipments  (id, data_id, user_uuid) 
                    VALUES      (?, ?, ?)
            """.trimIndent())

            loadUserEquipmentsStm = con.prepareStatement("""
                SELECT id, data_id FROM equipments WHERE user_uuid = ?
            """.trimIndent())
        }

        fun load(id: String): Equipment? {
            loadStm.setString(1, id)
            val rs = loadStm.executeQuery()
            if (rs.next()) {
                val dataId = rs.getString(1)
                val data = EquipmentData.get(dataId)
                if (data != null) return Equipment(id, data)
            }
            return null
        }

        private const val NUMBER_OF_UID_DIGITS = 9
        fun generate(data: EquipmentData, userUuid: String): Equipment {
            var m = 1; for (i in 0..NUMBER_OF_UID_DIGITS) { m *= 10 }
            val id = Random().nextInt(m).toString().padStart(NUMBER_OF_UID_DIGITS, '0')

            insertStm.setString(1, id)
            insertStm.setString(2, data.id)
            insertStm.setString(3, userUuid)
            insertStm.execute()

            return Equipment(id, data)
        }
    }

    fun save() {

    }


    fun getComponent(): Component {
        return Component.text("${equipmentData.name} [${uniqueId}]")
    }

    override fun toString(): String {
        return "Equipment{UniqueId: ${uniqueId}, EquipmentId: ${equipmentData.id}}"
    }
}