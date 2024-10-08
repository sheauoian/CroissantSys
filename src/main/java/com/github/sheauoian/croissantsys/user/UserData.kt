package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.util.BodyPart
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
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

        init {
            con.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS users(
                    uuid        TEXT        PRIMARY KEY,
                    money       INTEGER     DEFAULT $DEFAULT_MONEY,
                    health      REAL        DEFAULT $DEFAULT_HEALTH,
                    max_health  REAL        DEFAULT $DEFAULT_MAX_HEALTH
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

        fun get(uuid: UUID): UserData? {
            if (datum.containsKey(uuid)) return datum[uuid]
            add(uuid)
            if (datum.containsKey(uuid)) return datum[uuid]
            return null
        }

        fun getOnline(player: Player): UserDataOnline? {
            if (datum.containsKey(player.uniqueId)) return datum[player.uniqueId] as UserDataOnline
            return null
        }

        fun getLoadedAll(): List<UserData> {
            return datum.values.toList()
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
        val f = File(CroissantSys.instance.dataFolder, "userdata.yml")
        val c = YamlConfiguration.loadConfiguration(f)
        BodyPart.entries.forEach {
            wearing[it] = Equipment.load(c.getInt("$uuid.wearing.${it.name}"))
        }
    }
    fun reloadWearing() {
        BodyPart.entries.forEach {
            if (wearing[it] != null)
                wearing[it] = Equipment.load(wearing[it]!!.id)
        }
    }
    fun saveAndUnload() {
        save()
        datum.remove(uuid)
    }
    fun save() {
        saveStm.setString(1, uuid.toString())
        saveStm.setInt(2, money)
        saveStm.setDouble(3, health)
        saveStm.setDouble(4, maxHealth)
        saveStm.execute()
        saveWearing()
        if (this is UserDataOnline) {
            eManager.save()
        }
    }

    private fun saveWearing() {
        val f = File(CroissantSys.instance.dataFolder, "userdata.yml")
        val c = YamlConfiguration.loadConfiguration(f)

        for ((bodyPart, equipment) in wearing) {
            equipment?.save()
            c.set("$uuid.wearing.${bodyPart.name}", equipment?.id)
        }
        c.save(f)
    }

    // Wearing
    fun setWearing(bodyPart: BodyPart, equipment: Equipment): Boolean {
        if (equipment.data.bodyPart != bodyPart) return false
        wearing[bodyPart] = equipment
        return true
    }

    fun setWearing(equipment: Equipment) {
        wearing[equipment.data.bodyPart] = equipment
    }

    fun clearWearing() {
        wearing.clear()
    }

    fun getWearingComponent(): Component {
        var component: Component = Component.text("装備: ")
        BodyPart.entries.forEach {
            var comp = Component.text("${it.name}: ")
            val equipment = wearing[it]
            comp =
                if  (equipment != null)   comp.append(equipment.getComponent())
                else                      comp.append(Component.text("[ NONE ]"))
            component = component.append(comp.appendNewline())
        }
        return component
    }


    fun canBuy(required: Int): Boolean {
        return money >= required
    }
}