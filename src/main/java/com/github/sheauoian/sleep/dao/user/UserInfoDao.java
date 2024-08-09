package com.github.sheauoian.sleep.dao.user;

import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.player.UserInfo;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDao extends Dao {
    private final static UserInfoDao singleton = new UserInfoDao();
    public static UserInfoDao getInstance() { return singleton; }

    @Override
    public void createTable(){
        try {
            this.statement.execute(
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        try {
            this.statement.execute("DROP TABLE IF EXISTS user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        createTable();
    }

    public UserInfo insertAndGet(String uuid) {
        this.insert(uuid);
        return get(uuid);
    }

    public UserInfo get(String uuid) {
        try {
            ResultSet resultSet = statement.executeQuery(
                    String.format("select * from user where uuid = '%s' limit 1", uuid)
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
                return new UserInfo(
                        uuid,
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
            e.printStackTrace();
        }
        return null;
    }

    // Create new UserInfo Column if not exists.
    public void insert(String uuid) {
        try {
            String values = String.format(
                    "'%s','%s','%s'",
                    uuid,
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
            e.printStackTrace();
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
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
