package com.github.sheauoian.sleep.common.warppoint;

import com.github.sheauoian.sleep.dao.Dao;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnlockedWarpPointDao extends Dao {
    private static final UnlockedWarpPointDao singleton = new UnlockedWarpPointDao();
    private final PreparedStatement has, getUnlockedIds, insert;
    public UnlockedWarpPointDao() {
        createTable();
        try {
            has = connection.prepareStatement(
                    "SELECT uuid FROM unlocked_warp_points WHERE uuid = ? AND warp_point_id = ? LIMIT 1");
            getUnlockedIds = connection.prepareStatement(
                    "SELECT warp_point_id FROM unlocked_warp_points WHERE uuid = ?");
            insert = connection.prepareStatement(
                    "INSERT OR IGNORE INTO unlocked_warp_points (uuid, warp_point_id) VALUES (?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable() {
        try {
            this.statement.execute(
            """
                CREATE TABLE IF NOT EXISTS unlocked_warp_points
                (
                    uuid STRING NOT NULL,
                    warp_point_id STRING NOT NULL,
                    UNIQUE(uuid, warp_point_id)
                );
                """);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public boolean has(Player player, String id) {
        try {
            logger.info(id + ", " + player.getUniqueId());
            has.setString(1, player.getUniqueId().toString());
            has.setString(2, id);
            ResultSet rs = has.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
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
            logger.info(e.getMessage());
        }
        return null;
    }
    public void insert(Player player, WarpPoint warpPoint) {
        try {
            insert.setString(1, player.getUniqueId().toString());
            insert.setString(2, warpPoint.getId());
            insert.execute();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public void drop() {
        try {
            this.statement.execute(
                """
                    DROP TABLE unlocked_warp_points
                    """);
            createTable();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public static UnlockedWarpPointDao getInstance() {
        return singleton;
    }
}
