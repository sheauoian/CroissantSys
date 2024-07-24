package com.github.sheauoian.sleep.dao.item;

import com.github.sheauoian.sleep.common.item.Equipment;
import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.util.Rarity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDao extends Dao {
    private static final EquipmentDao singleton = new EquipmentDao();
    private final PreparedStatement getFromStorage, insertIntoStorage, insert;

    public EquipmentDao() {
        createTable();
        try {
            insertIntoStorage = connection.prepareStatement(
                    "INSERT INTO equipment_storage (uuid, item_id, rarity) VALUES (?, ?, ?)");
            getFromStorage = connection.prepareStatement(
                    "SELECT * FROM equipment_storage WHERE uuid = ?");
            insert = connection.prepareStatement(
                    "INSERT INTO equipment (item_id, rarity) VALUES (?, ?)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable() {
        try {
            this.statement.execute(
                    """
                        CREATE TABLE IF NOT EXISTS equipment
                        (
                            item_id STRING NOT NULL,
                            rarity INT NOT NULL,
                            lore STRING,
                            head_name STRING,
                            chest_name STRING,
                            leggings_name STRING,
                            boots_name STRING,
                            UNIQUE(item_id, rarity)
                        );
                        """);
            this.statement.execute(
                    """
                        CREATE TABLE IF NOT EXISTS equipment_storage
                        (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            uuid STRING NOT NULL,
                            item_id STRING NOT NULL,
                            rarity INT NOT NULL,
                            level INT DEFAULT 0,
                            option STRING
                        )
                        """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insert(String item_id, Rarity rarity) {
        insert(item_id, rarity.ordinal());
    }
    public void insert(String item_id, int rarity) {
        try {
            insert.setString(1, item_id);
            insert.setInt(2, rarity);
            insert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIntoStorage(String uuid, String item_id, Rarity rarity) {
        try {
            insertIntoStorage.setString(1, uuid);
            insertIntoStorage.setString(2, item_id);
            insertIntoStorage.setInt(3, rarity.ordinal());
            insertIntoStorage.execute();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public List<Equipment> getFromStorage(String uuid) {
        try {
            getFromStorage.setString(1, uuid);
            ResultSet rs = getFromStorage.executeQuery();
            List<Equipment> equips = new ArrayList<>();
            while (rs.next()) {
                equips.add(new Equipment(
                        rs.getInt("id"),
                        uuid,
                        rs.getString("item_id"),
                        rs.getInt("rarity"),
                        rs.getInt("level")
                        )
                );
            }
            return equips;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    public static EquipmentDao getInstance() {
        return singleton;
    }
}
