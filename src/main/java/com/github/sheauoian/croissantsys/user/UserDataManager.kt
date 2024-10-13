package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.Manager
import org.bukkit.entity.Player
import java.sql.PreparedStatement
import java.util.*

private const val DEFAULT_MONEY         = 1000
private const val DEFAULT_HEALTH        = 10.0
private const val DEFAULT_MAX_HEALTH    = 10.0

class UserDataManager: Manager<UUID, UserData>() {
    companion object {
        val instance = UserDataManager()
    }
    private val datum: MutableMap<UUID, UserData> = mutableMapOf()

    fun saveAll() {
        datum.values.forEach {
            it.save()
        }
        CroissantSys.instance.logger.info("Saved ${datum.size} users!")
    }


    private var loadStm: PreparedStatement
    private var saveStm: PreparedStatement
    private var insertStm: PreparedStatement

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

        insertStm = con.prepareStatement("""
                INSERT INTO 
                    users   (uuid) 
                    VALUES  (?)
        """.trimIndent())
    }

    private fun put(data: UserData): UserData {
        datum[data.uuid] = data
        return data
    }
    private fun put(data: UserDataOnline): UserDataOnline {
        datum[data.uuid] = data
        return data
    }

    private fun insertOnline(k: Player): UserDataOnline? {
        insertStm.setString(1, k.uniqueId.toString())
        insertStm.execute()
        return loadOnline(k)
    }


    fun join(k: Player): UserDataOnline? {
        val u = datum[k.uniqueId]
        if (u is UserDataOnline) {
            return u
        } else if (u != null) {
            save(u)
        }
        return loadOnline(k) ?: insertOnline(k)
    }

    fun quit(k: Player) {
        val u = datum[k.uniqueId]
        if (u != null) {
            u.save()
        }
        datum.remove(k.uniqueId)
    }

    override fun load(k: UUID): UserData? {
        if (datum.containsKey(k))
            return datum[k]
        loadFromDatabase(k)?.let {
            return put(it)
        }
        return null
    }

    private fun loadOnline(k: Player): UserDataOnline? {
        if (datum.containsKey(k.uniqueId)) {
            val d = datum[k.uniqueId]
            if (d is UserDataOnline)
                return d
        }
        loadOnlineFromDatabase(k)?.let {
            return put(it)
        }
        return null
    }

    private fun loadFromDatabase(k: UUID): UserData? {
        loadStm.setString(1, k.toString())
        val rs = loadStm.executeQuery()
        if (!rs.next()) return null

        val user = UserData(
            k,
            rs.getInt("money"),
            rs.getDouble("health"),
            rs.getDouble("max_health")
        )
        return user
    }
    private fun loadOnlineFromDatabase(k: Player): UserDataOnline? {
        loadStm.setString(1, k.toString())
        val rs = loadStm.executeQuery()
        if (!rs.next()) return null

        val user = UserDataOnline(
            k,
            rs.getInt("money"),
            rs.getDouble("health"),
            rs.getDouble("max_health")
        )
        return user
    }

    override fun save(v: UserData) {
        saveStm.setString(1, v.uuid.toString())
        saveStm.setInt(2, v.money)
        saveStm.setDouble(3, v.health)
        saveStm.setDouble(4, v.maxHealth)
        saveStm.execute()
        CroissantSys.instance.logger.info("UserData Saved: ${v.uuid}")
    }

    fun get(uuid: UUID): UserData? {
        if (datum.containsKey(uuid)) return datum[uuid]
        loadStm.setString(1, uuid.toString())
        if (loadStm.executeQuery().next()) {
            load(uuid)
            return datum[uuid]
        }
        return null
    }

    fun getOnline(player: Player): UserDataOnline? {
        if (datum.containsKey(player.uniqueId)) return datum[player.uniqueId] as UserDataOnline
        return null
    }

    fun getAll(): List<UserData> {
        return datum.values.toList()
    }
}