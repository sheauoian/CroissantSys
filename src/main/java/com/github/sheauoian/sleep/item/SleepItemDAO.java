package com.github.sheauoian.sleep.item;


import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SleepItemDAO {
    private Connection connection = null;
    private Statement statement = null;
    public SleepItemDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            String db_name = "database.db";
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + db_name);
            this.statement = connection.createStatement();
        } catch (Exception e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
    public void createTable(){
        try {
            this.statement.executeQuery("""
                            CREATE TABLE item
                            (
                                id STRING PRIMARY KEY,
                                name STRING NOT NULL,
                                item_type STRING DEFAULT 'PAPER',
                                category INT DEFAULT 0
                            )
                            """);
            this.connection.commit();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[SQL] テーブル 'item' は既に作成されています");
        }
    }
    public List<SleepItem> getAll(){
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM item");
            List<SleepItem> ret = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                ret.add(new SleepItem(id, name, item_type, category));
            }
            rs.close();
            return ret;
        } catch (SQLException e){
            Bukkit.getLogger().info(e.getMessage());
        }
        return null;
    }

    public SleepItem getByID(String id){
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM item WHERE id = '"+ id +"'");
            if (rs.next()) {
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                rs.close();
                return new SleepItem(id, name, item_type, category);
            }
        } catch (SQLException e){
            Bukkit.getLogger().info(e.getMessage());
        }
        return null;
    }

    public void insert(String id, String name, String item_type, int category){
        try {
            this.statement.executeUpdate(
                        "INSERT INTO item(id, name, item_type, category) " +
                            "VALUES('"+
                                id+"','"+
                                name+"','"+
                                item_type+"',"+
                                category+");"
            );
        } catch (SQLException e){
            Bukkit.getLogger().info(e.getMessage());
        }
    }
    public void dropTable(){
        try {
            this.statement.executeUpdate("DROP TABLE item");
        } catch (SQLException e){
            Bukkit.getLogger().info(e.getMessage());
        }
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
