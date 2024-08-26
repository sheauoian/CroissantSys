package com.github.sheauoian.croissantsys.common.item;

import com.github.sheauoian.croissantsys.common.storage.Storage;
import com.github.sheauoian.croissantsys.util.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StorageItemDao implements Dao {
    private static final StorageItemDao instance = new StorageItemDao();
    private final PreparedStatement createTable, get, getAll, update, insert;

    public static StorageItemDao getInstance() {
        return instance;
    }

    private StorageItemDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS item_storage
                    (
                        uuid text not null,
                        item_id text not null,
                        amount integer default 0,
                        UNIQUE(uuid, item_id)
                    )""");
            createTable.execute();
            get = con.prepareStatement("""
                    SELECT * FROM item_storage
                    WHERE
                        uuid = ? AND
                        item_id = ?
                    LIMIT 1""");
            getAll = con.prepareStatement("""
                    SELECT * FROM item_storage
                        WHERE uuid = ?""");
            update = con.prepareStatement("""
                    UPDATE item_storage SET amount = ?
                    WHERE
                        uuid = ? AND
                        item_id = ?""");
            insert = con.prepareStatement("""
                    INSERT OR IGNORE INTO item_storage (uuid, item_id, amount)
                    VALUES (?, ?, ?)""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        createTable();
    }
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StorageItem> getAll(String uuid) {
        try {
            List<StorageItem> items = new ArrayList<>();
            getAll.setString(1, uuid);
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                items.add(new StorageItem(rs.getString("item_id"), rs.getInt("amount")));
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public StorageItem get(String uuid, String item_id) {
        try {
            get.setString(1, uuid);
            get.setString(2, item_id);
            ResultSet resultSet = get.executeQuery();
            if (resultSet.next()) {
                return new StorageItem(
                        item_id,
                        resultSet.getInt("amount")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new StorageItem(item_id, 0);
    }

    public void update(String uuid, Storage storage) {
        try {

            for (StorageItem item : storage.getStorageItems()) {
                update.setInt(1, item.getAmount());
                update.setString(2, uuid);
                update.setString(3, item.getId());
                update.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insert(String uuid, StorageItem item) {
        try {
            insert.setString(1, uuid);
            insert.setString(2, item.getId());
            insert.setLong(3, item.getAmount());
            insert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop(){
        try {
            con.createStatement().executeUpdate("DROP TABLE item_storage;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
