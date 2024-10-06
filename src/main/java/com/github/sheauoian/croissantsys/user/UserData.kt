package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.util.BodyPart
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.sql.PreparedStatement
import java.util.*

open class UserData(val uuid: UUID): DbDriver() {
    companion object {
        // 初期ステータス
        private const val DEFAULT_MONEY         = 1000
        private const val DEFAULT_HEALTH        = 10.0
        private const val DEFAULT_MAX_HEALTH    = 10.0

        // SQL
        var loadStm: PreparedStatement
        var saveStm: PreparedStatement
        var loadWearingsStm: PreparedStatement
        var saveWearingsStm: PreparedStatement
        var deleteWearingStm: PreparedStatement

        init {
            con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS users(
                    uuid        TEXT        PRIMARY KEY,
                    money       INT         DEFAULT $DEFAULT_MONEY,
                    health      REAL        DEFAULT $DEFAULT_HEALTH,
                    max_health  REAL        DEFAULT $DEFAULT_MAX_HEALTH
                )
            """.trimIndent())

            con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS wearings(
                    user_uuid           TEXT        NOT NULL,
                    body_part           INT         DEFAULT 0,
                    equipment_id        TEXT        NOT NULL,
                    UNIQUE(user_uuid, body_part)
                )
            """.trimIndent())

            loadStm = con.prepareStatement("""
                SELECT * FROM users WHERE uuid = ?
                """.trimIndent())

            saveStm = con.prepareStatement("""
                INSERT INTO 
                    users   (uuid, money, health, max_health) 
                    VALUES  (?, ?, ?, ?)
                ON CONFLICT(uuid)
                    DO UPDATE SET 
                        money=excluded.money, 
                        health=excluded.health,
                        max_health=excluded.max_health
                """.trimIndent())

            loadWearingsStm = con.prepareStatement("""
                SELECT body_part, equipment_id FROM wearings WHERE user_uuid = ?
                """.trimIndent())

            saveWearingsStm = con.prepareStatement("""
                INSERT INTO 
                    wearings    (user_uuid, body_part, equipment_id) 
                    VALUES      (?, ?, ?)
                ON CONFLICT(user_uuid, body_part)
                    DO UPDATE SET 
                        equipment_id=excluded.equipment_id
            """.trimIndent())

            deleteWearingStm = con.prepareStatement("""
                DELETE FROM wearings WHERE user_uuid = ? AND body_part = ?
            """.trimIndent())
        }

        val datum: MutableMap<UUID, UserData> = mutableMapOf()

        fun add(uuid: UUID) {
            val player = Bukkit.getServer().getPlayer(uuid)
            if (player != null && player.isOnline)
                datum[uuid] = UserDataOnline(player)
            else
                datum[uuid] = UserData(uuid)
        }
        fun addOnline(player: Player) {
            datum[player.uniqueId] = UserDataOnline(player)
        }
        fun addOffline(uuid: UUID) {
            datum[uuid] = UserData(uuid)
        }
        fun getOnline(player: Player): UserDataOnline {
            return datum[player.uniqueId] as UserDataOnline
        }

        fun save() {
            datum.values.forEach {
                it.save()
            }
            CroissantSys.instance.logger.info("Saved ${datum.size} users!")
        }
    }

    constructor(uuidString: String): this(UUID.fromString(uuidString))

    var money: Int = DEFAULT_MONEY
    var health: Double = DEFAULT_HEALTH
    var maxHealth: Double = DEFAULT_MAX_HEALTH

    val wearing: MutableMap<BodyPart, Equipment?> = EnumMap(BodyPart::class.java)

    init {
        loadStm.setString(1, uuid.toString())
        val rs = loadStm.executeQuery()
        if (rs.next()) {
            money = rs.getInt("money")
            health = rs.getDouble("health")
            maxHealth = rs.getDouble("max_health")
        }

        loadWearingsStm.setString(1, uuid.toString())
        val wearingRs = loadWearingsStm.executeQuery()
        while (wearingRs.next()) {
            val bodyPart = BodyPart.entries[wearingRs.getInt(1)]
            val equipment = Equipment.load(wearingRs.getString(2))
            wearing[bodyPart] = equipment
        }
    }

    fun save() {
        saveStm.setString(1, uuid.toString())
        saveStm.setInt(2, money)
        saveStm.setDouble(3, health)
        saveStm.setDouble(4, maxHealth)
        saveStm.execute()

        for ((bodyPart, equipment) in wearing) {
            if (equipment != null) {
                saveWearingsStm.setString(1, uuid.toString())
                saveWearingsStm.setString(2, bodyPart.name)
                saveWearingsStm.setString(3, equipment.uniqueId)
                saveWearingsStm.execute()
                equipment.save()
            }
            else {
                deleteWearingStm.setString(1, uuid.toString())
                deleteWearingStm.setString(2, bodyPart.name)
                deleteWearingStm.execute()
            }
        }
    }

    fun setWearing(bodyPart: BodyPart, equipment: Equipment) {
        wearing[bodyPart] = equipment
        if (this is UserDataOnline) {
            player.sendMessage("装備が良い具合に設定出来たかも ${bodyPart}, ${wearing[bodyPart] ?: "none"}")
        }
    }

    fun clearWearing() {
        wearing.clear()
    }

    fun getWearingComponent(): Component {
        var component: Component = Component.text("装備: ")
        BodyPart.entries.forEach {
            var comp = Component.text("${it.name}: ")
            val equipment = wearing[it]
            if (equipment != null) comp = comp.append(equipment.getComponent())
            else comp = comp.append(Component.text("[ NONE ]"))
            component = component.append(comp.appendNewline())
        }
        return component
    }

    fun canBuy(required: Int): Boolean {
        return money >= required
    }
}