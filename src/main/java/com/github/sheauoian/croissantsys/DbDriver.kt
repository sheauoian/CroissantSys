package com.github.sheauoian.croissantsys

import org.sqlite.SQLiteConfig
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

open class DbDriver {
    fun close() {
        try {
            con.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        val instance: DbDriver = DbDriver()
        var con: Connection

        init {
            try {
                val config = SQLiteConfig()
                config.setDateStringFormat("yyyy-MM-dd HH:mm:ss")
                con = DriverManager.getConnection("jdbc:sqlite:database.db", config.toProperties())
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}