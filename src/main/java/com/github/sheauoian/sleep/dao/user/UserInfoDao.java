package com.github.sheauoian.sleep.dao.user;

import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.player.SleepPlayer;
import com.github.sheauoian.sleep.player.UserInfo;
import io.papermc.paper.annotation.DoNotUse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDao extends Dao {
    private final static UserInfoDao singleton = new UserInfoDao();
    @Override
    public void createTable(){
        try {
            this.statement.executeQuery(
                    """
                        CREATE TABLE IF NOT EXISTS user
                        (
                            uuid STRING PRIMARY KEY,
                            first_login TEXT NOT NULL,
                            last_login TEXT NOT NULL,
                            level INTEGER DEFAULT 0,
                            xp REAL DEFAULT 0,
                            strength REAL DEFAULT 1,
                            defence REAL DEFAULT 1,
                            health REAL DEFAULT 10,
                            max_health REAL DEFAULT 10
                        );
                        """);
            this.connection.commit();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }


    // For Newcomers.
    public UserInfo insertAndGet(OfflinePlayer player) {
        this.insert(player);
        return get(player);
    }
    // Get an UserInfo Column.
    public UserInfo get(OfflinePlayer player) {
        try {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "select * from user where uuid = '%s' limit 1",
                    player.getUniqueId()
                    )
            );
            if (resultSet.next()) {
                String first_login = resultSet.getString("first_login");
                String last_login  = resultSet.getString("last_login");
                int level = resultSet.getInt("level");
                float xp = resultSet.getFloat("xp");
                float strength = resultSet.getFloat("strength");
                float defence = resultSet.getFloat("defence");
                float health = resultSet.getFloat("health");
                float max_health = resultSet.getFloat("max_health");
                if (player instanceof Player)
                    return new SleepPlayer(
                        player.getUniqueId(),
                        first_login,
                        last_login,
                        level,
                        xp,
                        strength,
                        defence,
                        health,
                        max_health,
                        (Player) player
                );
                return new UserInfo(
                        player.getUniqueId(),
                        first_login,
                        last_login,
                        level,
                        xp,
                        strength,
                        defence,
                        health,
                        max_health
                );
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return null;
    }
    // Create new UserInfo Column if not exists.
    public void insert(OfflinePlayer p) {
        try {
            String values = String.format(
                    "'%s','%s','%s'",
                    p.getUniqueId(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            String sql = String.format("""
                        INSERT OR IGNORE INTO user
                        (
                            uuid,
                            first_login,
                            last_login
                        ) VALUES (%s);
                        """, values);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    // Update an UserInfo Column.
    public void update(UserInfo info) {
        try {
            String sql = String.format("""
                        UPDATE user SET
                            last_login = '%s',
                            level = %s,
                            xp = %s,
                            strength = %s,
                            defence = %s,
                            health = %s,
                            max_health = %s
                        WHERE uuid = '%s'
                        """,
                    LocalDateTime.now(),
                    info.getLevel(),
                    info.getXp(),
                    info.getStrength(),
                    info.getDefence(),
                    info.getHealth(),
                    info.getMaxHealth(),
                    info.uuid);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }


    public static UserInfoDao getInstance() {
        return singleton;
    }
}
