package com.github.sheauoian.croissantsys.world

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.util.Manager
import org.bukkit.Location
import java.sql.PreparedStatement

class WarpPointManager: Manager<String, WarpPoint>() {
    companion object {
        val instance = WarpPointManager()
    }

    private val saveStm: PreparedStatement
    private val loadStm: PreparedStatement

    init {
        con.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS warp_point (
                id      STRING  PRIMARY KEY,
                w       STRING  NOT NULL,
                x       REAL    NOT NULL,
                y       REAL    NOT NULL,
                z       REAL    NOT NULL,
                yaw     REAL    NOT NULL,
                pitch   REAL    NOT NULL
            )
        """.trimIndent())

        loadStm = con.prepareStatement("""
            SELECT w, x, y, z, yaw, pitch FROM warp_point WHERE id = ?
        """.trimIndent())

        saveStm = con.prepareStatement("""
            INSERT INTO warp_point(id, w, x, y, z, yaw, pitch) VALUES(?, ?, ?, ?, ?, ?, ?)
        """.trimIndent())
    }

    fun reload() {
        val rs = con.createStatement().executeQuery("""
            SELECT id FROM warp_point
        """.trimIndent())
        while (rs.next()) {
            loadFromDatabase(rs.getString(1))
        }
    }


    private val datum: MutableMap<String, WarpPoint> = mutableMapOf()

    fun find(k: String): WarpPoint? {
        return datum[k]
    }

    override fun save(v: WarpPoint) {
        saveStm.setString(1, v.id)
        saveStm.setString(2, v.location.world.name)
        saveStm.setDouble(3, v.location.x)
        saveStm.setDouble(4, v.location.y)
        saveStm.setDouble(5, v.location.z)
        saveStm.setFloat(6, v.location.yaw)
        saveStm.setFloat(7, v.location.pitch)
        saveStm.executeUpdate()
    }

    override fun load(k: String): WarpPoint? {
        return datum[k] ?: loadFromDatabase(k)
    }

    private fun loadFromDatabase(k: String): WarpPoint? {
        loadStm.setString(1, k)
        val rs = loadStm.executeQuery()
        if (rs.next()) {
            val l = Location(
                CroissantSys.instance.server.getWorld(rs.getString(1)),
                rs.getDouble(2),
                rs.getDouble(3),
                rs.getDouble(4),
                rs.getFloat(5),
                rs.getFloat(6)
            )
            datum[k] = WarpPoint(k, l)
            return datum[k]
        }
        return null
    }

    fun getIds(): Collection<String> {
        return datum.keys
    }

    fun getAll(): Collection<WarpPoint> {
        return datum.values
    }
}