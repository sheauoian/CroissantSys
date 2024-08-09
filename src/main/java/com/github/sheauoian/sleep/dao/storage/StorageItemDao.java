package com.github.sheauoian.sleep.dao.storage;

import com.github.sheauoian.sleep.common.storage.Storage;
import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.common.item.StorageItem;
import com.github.sheauoian.sleep.player.OnlineUser;
import com.github.sheauoian.sleep.player.UserInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StorageItemDao extends Dao {
    private static final StorageItemDao singleton = new StorageItemDao();
    @Override
    public void createTable() {
        try {
            String sql =
                    """
                    CREATE TABLE IF NOT EXISTS item_storage
                    (
                        uuid text not null,
                        item_id text not null,
                        amount integer default 0,
                        UNIQUE(uuid, item_id)
                    );
                    """;
            connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StorageItem> getAll(String uuid) {
        try {
            List<StorageItem> items = new ArrayList<>();
            String sql =
                    """
                    SELECT * FROM item_storage
                        WHERE uuid = ?
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                items.add(new StorageItem(rs.getString("item_id"), rs.getInt("amount")));
            }
            return items;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public StorageItem get(String uuid, String item_id) {
        try {
            String sql =
                    """
                    SELECT * FROM item_storage
                        WHERE uuid = ? AND item_id = ? LIMIT 1
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, uuid);
            st.setString(2, item_id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return new StorageItem(
                        item_id,
                        resultSet.getInt("amount")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new StorageItem(
                item_id,
                0
        );
    }

    public void update(String uuid, Storage storage) {
        try {
            String sql =
                    """
                    UPDATE item_storage SET amount = ?
                        WHERE uuid = ? AND item_id = ?
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            for (StorageItem item : storage.getStorageItems()) {
                logger.info(item.toString());
                st.setInt(1, item.getAmount());
                st.setString(2, uuid);
                st.setString(3, item.getId());
                st.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insert(String uuid, StorageItem item) {
        try {
            String sql =
                    """
                    INSERT OR IGNORE INTO item_storage (uuid, item_id, amount)
                        VALUES (?, ?, ?);
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, uuid);
            st.setString(2, item.getId());
            st.setLong(3, item.getAmount());
            st.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void dropTable(){
        try {
            this.statement.executeUpdate("DROP TABLE item_storage;");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static StorageItemDao getInstance() {
        return singleton;
    }
}
