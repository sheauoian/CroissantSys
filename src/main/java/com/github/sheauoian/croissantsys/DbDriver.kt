package com.github.sheauoian.croissantsys;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.sheauoian.croissantsys.CroissantSys.logger;

public class DbDriver {
    private static final DbDriver instance = new DbDriver();
    private final Connection con;
    public DbDriver() {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setDateStringFormat("yyyy-MM-dd HH:mm:ss");
            con = DriverManager.getConnection("jdbc:sqlite:database.db", config.toProperties());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static DbDriver getInstance() {
        return instance;
    }
    public Connection getConnection() {
        return con;
    }

    public void close(){
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
    }
}
