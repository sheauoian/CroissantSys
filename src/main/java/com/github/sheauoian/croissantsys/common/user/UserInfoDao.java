package com.github.sheauoian.croissantsys.common.user;

import com.github.sheauoian.croissantsys.util.Dao;
import com.github.sheauoian.croissantsys.util.StringDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInfoDao implements Dao {
    private final static UserInfoDao instance = new UserInfoDao();
    private final PreparedStatement createTable, drop, insert, get, getLoginDate, update, getAllUUIDs;

    public UserInfoDao() {
        try {
            createTable = con.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS user
                    (
                        uuid            STRING      PRIMARY KEY,
                        first_login     STRING      NOT NULL,
                        last_login      STRING      NOT NULL,
                        level           INTEGER     DEFAULT 0,
                        xp              REAL        DEFAULT 0,
                        hp              REAL        DEFAULT 10
                    )""");
            createTable.execute();
            drop = con.prepareStatement("""
                    DROP TABLE IF EXISTS user""");
            insert = con.prepareStatement("""
                    INSERT OR IGNORE INTO user(uuid, first_login, last_login)
                    VALUES (?, ?, ?)""");
            get = con.prepareStatement("""
                    SELECT * FROM user WHERE uuid = ? LIMIT 1""");
            getLoginDate = con.prepareStatement("""
                    SELECT first_login, last_login FROM user WHERE uuid = ? LIMIT 1""");
            update = con.prepareStatement("""
                    UPDATE user SET
                        last_login = ?,
                        level = ?,
                        xp = ?,
                        hp = ?
                    WHERE uuid = ?""");
            getAllUUIDs = con.prepareStatement("""
                    SELECT uuid FROM user""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserInfoDao getInstance() { return instance; }

    @Override
    public void init() {
        createTable();
    }

    public void createTable(){
        try {
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void drop() {
        try {
            drop.execute();
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(String uuid) {
        try {
            String now = StringDate.now();
            insert.setString(1, uuid);
            insert.setString(2, now);
            insert.setString(3, now);
            insert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserInfo insertAndGet(String uuid) {
        insert(uuid);
        return get(uuid);
    }

    public UserInfo get(String uuid) {
        try {
            get.setString(1, uuid);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                return new UserInfo(
                        uuid,
                        rs.getInt("level"),
                        rs.getInt("xp"),
                        rs.getInt("hp")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Date[] getLoginDate(String uuid) {
        try {
            get.setString(1, uuid);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                return new Date[]{rs.getDate(1), rs.getDate(2)};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    // Update an UserInfo Column.
    public void update(UserInfo info) {
        try {
            String now = StringDate.now();
            update.setString(1, now);
            update.setInt(2, info.getLevel());
            update.setFloat(3, info.getXp());
            update.setDouble(4, info.getHealth());
            update.setString(5, info.uuid);
            update.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllUUIDs() {
        try {
            List<String> list = new ArrayList<>();
            ResultSet rs = getAllUUIDs.executeQuery();
            while (rs.next()) list.add(rs.getString(1));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
