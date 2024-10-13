package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.DbDriver
import java.sql.PreparedStatement

class CMaterialStorageManager {
    companion object {
        val instance = CMaterialStorageManager()
    }

    private val loadStm: PreparedStatement
    private val saveStm: PreparedStatement
    init {
        DbDriver.con.createStatement().executeUpdate("""
            CREATE TABLE IF NOT EXISTS materials(
                    user_uuid       TEXT    NOT NULL,
                    material_id     INTEGER NOT NULL,
                    quantity        INTEGER DEFAULT 0,
                    UNIQUE(user_uuid, material_id)
                )
        """.trimIndent())

        loadStm = DbDriver.con.prepareStatement("""
            SELECT material_id, quantity FROM materials WHERE user_uuid = ?
        """.trimIndent())
        saveStm = DbDriver.con.prepareStatement("""
            INSERT INTO 
                materials   (user_uuid, material_id, quantity) 
                VALUES      (?, ?, ?)
            ON CONFLICT(user_uuid, material_id)
                DO UPDATE SET 
                    quantity=excluded.quantity
        """.trimIndent())
    }

    fun reload(storage: CMaterialStorage) {
        loadStm.setString(1, storage.uuid)
        val rs = loadStm.executeQuery()
        while (rs.next()) {
            storage.set(rs.getInt(1), rs.getLong(2))
        }
    }

    fun save(u: String, m: Int, q: Long) {
        saveStm.setString(1, u)
        saveStm.setInt(2, m)
        saveStm.setLong(3, q)
        saveStm.executeUpdate()
    }
}