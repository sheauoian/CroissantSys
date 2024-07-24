package com.github.sheauoian.sleep.dao;

import com.github.sheauoian.sleep.common.warppoint.WarpPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.sheauoian.sleep.Sleep;

public class WarpPointDao extends Dao {
    private static final WarpPointDao singleton = new WarpPointDao();
    private final PreparedStatement get, getAll, insert;
    public WarpPointDao() {
        createTable();
        try {
            get = connection.prepareStatement("SELECT * FROM warp_points WHERE id = ?");
            getAll = connection.prepareStatement("SELECT * FROM warp_points");
            insert = connection.prepareStatement("INSERT OR IGNORE INTO warp_points (id, name, world, x, y, z, r) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable() {
        try {
            this.statement.execute(
                    """
                        CREATE TABLE IF NOT EXISTS warp_points
                        (
                            id STRING PRIMARY KEY,
                            name STRING NOT NULL,
                            world STRING NOT NULL,
                            x INTEGER NOT NULL,
                            y INTEGER NOT NULL,
                            z INTEGER NOT NULL,
                            r REAL NOT NULL,
                            color_1 STRING,
                            color_2 STRING
                        );
                        """);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public WarpPoint get(String id) {
        try {
            get.setString(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                World world = Bukkit.getWorld(rs.getString("world"));
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                Location loc = new Location(world, x, y, z);
                loc.setYaw(rs.getFloat("r"));
                return new WarpPoint(id, name, loc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<WarpPoint> getAll() {
        try {
            ResultSet rs = getAll.executeQuery();
            List<WarpPoint> warpPoints = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                World world = Bukkit.getWorld(rs.getString("world"));
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                Location loc = new Location(world, x, y, z);
                loc.setYaw(rs.getFloat("r"));
                warpPoints.add(new WarpPoint(id, name, loc));
            }
            return warpPoints;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertAll() {
        try {
            for (WarpPoint warpPoint : Sleep.warpPointManager.getAll()) {
                insert.setString(1, warpPoint.getId());
                insert.setString(2, warpPoint.getName());
                insert.setString(3, warpPoint.getLocation().getWorld().getName());
                insert.setInt(4, warpPoint.getLocation().getBlockX());
                insert.setInt(5, warpPoint.getLocation().getBlockY());
                insert.setInt(6, warpPoint.getLocation().getBlockZ());
                insert.setFloat(7, warpPoint.getLocation().getYaw());
                insert.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        try {
            this.statement.execute(
                    """
                        DROP TABLE warp_points
                        """);
            createTable();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public static WarpPointDao getInstance() {
        return singleton;
    }
}
