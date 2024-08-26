package com.github.sheauoian.croissantsys.common.item.equipment;

import com.github.sheauoian.croissantsys.util.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EquipmentDao implements Dao {
    private static final EquipmentDao instance = new EquipmentDao();
    private final PreparedStatement createTable, save, get, getAll, getAllIds, insert, delete;
    private static int KEY;

    public EquipmentDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS equipment (
                        id              INTEGER PRIMARY KEY,
                        uuid            STRING  NOT NULL,
                        equipment_id    STRING  DEFAULT 'UNKNOWN',
                        level           INTEGER DEFAULT 0
                    )""");
            createTable.execute();
            save = con.prepareStatement("""
                    UPDATE equipment SET
                        level = ?
                    WHERE id = ?""");
            get = con.prepareStatement("""
                    SELECT uuid, equipment_id, level FROM equipment
                    WHERE id = ?""");
            getAll = con.prepareStatement("""
                    SELECT id, equipment_id, level FROM equipment
                    WHERE uuid = ?""");
            insert = con.prepareStatement("""
                    INSERT INTO equipment(id, uuid, equipment_id)
                    values(?, ?, ?)""");
            delete = con.prepareStatement("""
                    DELETE FROM equipment WHERE id = ?""");
            getAllIds = con.prepareStatement("""
                    SELECT id FROM equipment""");
            KEY = con.createStatement().executeQuery("SELECT MAX(id) FROM equipment").getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static EquipmentDao getInstance() {
        return instance;
    }

    // Create Table
    private void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update
    public void save(Equipment equipment) {
        try {
            save.setInt(1, equipment.getLevel());
            save.setInt(2, equipment.getId());
            save.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(String uuid, String equipment_id) {
        KEY ++;
        try {
            // TODO やや冗長
            insert.setInt(1, KEY);
            insert.setString(2, uuid);
            insert.setString(3, equipment_id);
            insert.execute();
            Equipment e = new Equipment(KEY, uuid, equipment_id, 0);
            int c = new Random().nextInt(2) + 1;
            for (int i = 0; i < c; i++) {
                e.statusUp();
            }
            e.save();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get
    public Equipment get(int id) {
        try {
            get.setInt(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                return new Equipment(
                    id,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Equipment> getAll(String uuid) {
        try {
            getAll.setString(1, uuid);
            ResultSet rs = getAll.executeQuery();
            List<Equipment> list = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                String equipment_id = rs.getString(2);
                int level = rs.getInt(3);
                list.add(new Equipment(id, uuid, equipment_id, level));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllIds() {
        try {
            ResultSet rs = getAllIds.executeQuery();
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(String.valueOf(rs.getInt(1)));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            delete.setInt(1, id);
            delete.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void init() {
        EquipmentInfo.init();
    }

    public List<String> getAllIds(String uuid) {
        try {
            getAll.setString(1, uuid);
            ResultSet rs = get.executeQuery();
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(String.valueOf(rs.getInt(1)));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}