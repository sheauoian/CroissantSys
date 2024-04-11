package com.github.sheauoian.sleep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.github.sheauoian.sleep.Sleep.logger;

public class DbDriver {
    private static final DbDriver DRIVER = new DbDriver();
    private Connection connection;
    private Statement statement;
    public DbDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            this.statement = connection.createStatement();
        } catch (Exception e){
            logger.warning(e.getMessage());
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public Statement getStatement() {
        return statement;
    }
    public static DbDriver singleton() {
        return DRIVER;
    }
    public void closeConnection(){
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
