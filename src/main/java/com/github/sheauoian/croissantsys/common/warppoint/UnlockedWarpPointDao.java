package com.github.sheauoian.croissantsys.common.warppoint;

import com.github.sheauoian.croissantsys.util.Dao;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnlockedWarpPointDao implements Dao {
    private static final UnlockedWarpPointDao singleton = new UnlockedWarpPointDao();
    private final PreparedStatement createTable, has, getUnlockedIds, insert;

    @Override
    public void init() {
        createTable();
    }
    public UnlockedWarpPointDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS unlocked_warp_points
                    (
                        uuid            STRING NOT NULL,
                        warp_point_id   STRING NOT NULL,
                        UNIQUE(uuid, warp_point_id)
                    )""");
            createTable.execute();
            has = con.prepareStatement(
                    "SELECT uuid FROM unlocked_warp_points WHERE uuid = ? AND warp_point_id = ? LIMIT 1");
            getUnlockedIds = con.prepareStatement(
                    "SELECT warp_point_id FROM unlocked_warp_points WHERE uuid = ?");
            insert = con.prepareStatement(
                    "INSERT OR IGNORE INTO unlocked_warp_points (uuid, warp_point_id) VALUES (?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean has(Player player, String id) {
        try {
            has.setString(1, player.getUniqueId().toString());
            has.setString(2, id);
            ResultSet rs = has.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getUnlockedIds(Player player) {
        try {
            getUnlockedIds.setString(1, player.getUniqueId().toString());
            ResultSet rs = getUnlockedIds.executeQuery();
            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString("warp_point_id"));
            }
            return ids;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insert(Player player, WarpPoint warpPoint) {
        try {
            insert.setString(1, player.getUniqueId().toString());
            insert.setString(2, warpPoint.getId());
            insert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnlockedWarpPointDao getInstance() {
        return singleton;
    }
}
