package com.github.sheauoian.sleep.dao.user;

import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.player.UserInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class UserInfoDao extends Dao {
    private final static UserInfoDao singleton = new UserInfoDao();
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
    public UserInfo getOnline_ByUUID(UUID uuid) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM user WHERE uuid = '"+ uuid +"' LIMIT 1");
            if (!rs.next()) {
                this.insert_newUserInfo(uuid.toString());
            }
            Player player = Bukkit.getServer().getPlayer(uuid);
            String first_login = rs.getString("first_login");
            String last_login  = rs.getString("last_login");
            int level = rs.getInt("level");
            float xp = rs.getFloat("xp");
            float strength = rs.getFloat("strength");
            float defence = rs.getFloat("defence");
            float health = rs.getFloat("health");
            float max_health = rs.getFloat("max_health");
            rs.close();
            return new UserInfo(
                    player,
                    uuid.toString(),
                    first_login,
                    last_login,
                    level,
                    xp,
                    strength,
                    defence,
                    health,
                    max_health
            );
        } catch (SQLException e){
            logger.info(e.getMessage());
        }
        return null;
    }
    public void insert_newUserInfo(String uuid) {
        try {
            String values = String.format(
                    "'%s','%s','%s'",
                    uuid,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            String sql = String.format("""
                        INSERT INTO user
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

    public void update(UserInfo info) {
        try {
            String sql = String.format("""
                        UPDATE user SET
                            last_login = '%s',
                            strength = %s,
                            defence = %s,
                            health = %s,
                            max_health = %s
                        WHERE uuid = '%s'
                        """,
                    LocalDateTime.now(),
                    info.getStrength(),
                    info.getDefence(),
                    info.getHealth(),
                    info.getMaxHealth(),
                    info.player.getUniqueId());
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public static UserInfoDao getInstance() {
        return singleton;
    }
}
