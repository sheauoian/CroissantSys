package com.github.sheauoian.sleep.dao.storage;

import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.common.item.StorageItem;
import com.github.sheauoian.sleep.player.OnlineUser;

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
                        uuid,
                        item_id,
                        resultSet.getInt("amount")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new StorageItem(
                uuid,
                item_id,
                0
        );
    }

    public boolean add(OnlineUser user, String item_id, int add) {
        boolean res = user.storageItem.containsKey(item_id);
        if (!res) {
            StorageItem item = new StorageItem(
                    user.info.uuid,
                    item_id,
                    0);
            try {
                String sql =
                        """
                        SELECT amount FROM item_storage
                            WHERE uuid = ? AND item_id = ? LIMIT 1
                        """;
                PreparedStatement st = connection.prepareStatement(sql);
                st.setString(1, user.info.uuid);
                st.setString(2, item_id);
                ResultSet resultSet = st.executeQuery();
                if (resultSet.next()) {
                    item.amount = resultSet.getInt("amount");
                    res = true;
                } else {
                    insert(item);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            user.storageItem.put(item_id, get(user.info.uuid, item_id));
        }
        user.storageItem.get(item_id).amount += add;
        return res;
    }
    public void update(Collection<StorageItem> storageItem) {
        try {
            String sql =
                    """
                    UPDATE item_storage SET amount = ?
                        WHERE uuid = ? AND item_id = ?
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            for (StorageItem item : storageItem) {
                logger.info(item.toString());
                st.setLong(1, item.amount);
                st.setString(2, item.uuid.toString());
                st.setString(3, item.item_id);
                st.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insert(StorageItem item) {
        try {
            String sql =
                    """
                    INSERT OR IGNORE INTO item_storage (uuid, item_id, amount)
                        VALUES (?, ?, ?);
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, item.uuid.toString());
            st.setString(2, item.item_id);
            st.setLong(3, item.amount);
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
