package com.github.sheauoian.croissantsys.common.item;
import com.github.sheauoian.croissantsys.util.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerItemDao implements Dao {
    private static final ServerItemDao instance = new ServerItemDao();
    private final PreparedStatement createTable, getAll, get, insert;

    public static ServerItemDao getInstance() {
        return instance;
    }

    private ServerItemDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS item
                    (
                        id          STRING  PRIMARY KEY,
                        name        STRING  NOT NULL,
                        item_type   STRING  DEFAULT 'PAPER',
                        category    INT     DEFAULT 0
                    )""");
            createTable.execute();
            getAll = con.prepareStatement("""
                    SELECT * FROM item""");
            get = con.prepareStatement("""
                    SELECT * FROM item WHERE id = ?""");
            insert = con.prepareStatement("""
                    INSERT INTO item (id, name, item_type, category)
                    VALUES (?, ?, ?, ?)""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        createTable();
    }
    public void createTable(){
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<ServerItem> getAll(){
        try {
            ResultSet rs = getAll.executeQuery();
            List<ServerItem> ret = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                ret.add(new ServerItem(
                        id,
                        name,
                        item_type,
                        ItemCategory.values()[category].name()
                ));
            }
            rs.close();
            return ret;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ServerItem get(String id){
        try {
            get.setString(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                rs.close();
                return new ServerItem(
                        id,
                        name,
                        item_type,
                        ItemCategory.values()[category].name()
                );
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void insert(String id, String name, String item_type, int category){
        try {
            insert.setString(1, id);
            insert.setString(2, name);
            insert.setString(3, item_type);
            insert.setInt(4, category);
            insert.execute();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void dropTable(){
        try {
            con.createStatement().executeUpdate("DROP TABLE item");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
