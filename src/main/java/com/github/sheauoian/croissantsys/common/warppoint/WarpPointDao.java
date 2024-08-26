package com.github.sheauoian.croissantsys.common.warppoint;

import com.github.sheauoian.croissantsys.util.Dao;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarpPointDao implements Dao {
    private static final WarpPointDao instance = new WarpPointDao();
    private final PreparedStatement get, getAll, insert, createTable, drop;

    @Override
    public void init() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static WarpPointDao getInstance() {
        return instance;
    }

    public WarpPointDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS warps
                    (
                        id      STRING      PRIMARY KEY,
                        name    STRING      NOT NULL,
                        world   STRING      NOT NULL,
                        x       INTEGER     NOT NULL,
                        y       INTENER     NOT NULL,
                        z       INTEGER     NOT NULL,
                        r       REAL        NOT NULL
                    )""");
            createTable.execute();
            get = con.prepareStatement("""
                    SELECT * FROM warps
                    WHERE id = ?""");
            getAll = con.prepareStatement("""
                    SELECT * FROM warps""");
            insert = con.prepareStatement("""
                    INSERT OR IGNORE INTO warps (id, name, world, x, y, z, r)
                    VALUES (?, ?, ?, ?, ?, ?, ?)""");
            drop = con.prepareStatement("""
                    DROP TABLE warps""");
            init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            for (WarpPoint warpPoint : WarpPointManager.getInstance().getAll()) {
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

    // DROP
    public void drop() {
        try {
            drop.execute();
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
