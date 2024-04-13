package com.github.sheauoian.sleep.dao.item;
import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.Dao;
import com.github.sheauoian.sleep.item.SleepItem;
import org.bukkit.Bukkit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SleepItemDao extends Dao {
    private static final SleepItemDao singleton = new SleepItemDao();
    public void createTable(){
        try {
            this.statement.execute(
                            """
                                CREATE TABLE IF NOT EXISTS item
                                (
                                    id STRING PRIMARY KEY,
                                    name STRING NOT NULL,
                                    item_type STRING DEFAULT 'PAPER',
                                    category INT DEFAULT 0
                                )
                                """);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public List<SleepItem> getAll(){
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM item");
            List<SleepItem> ret = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                logger.info(name);
                ret.add(new SleepItem(id, name, item_type, category));
            }
            rs.close();
            return ret;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public SleepItem getByID(String id){
        try {
            String sql =
                    """
                    SELECT * FROM item WHERE id = ?;
                    """;
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String item_type = rs.getString("item_type");
                int category = rs.getInt("category");
                rs.close();
                return new SleepItem(id, name, item_type, category);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void insert(String id, String name, String item_type, int category){
        try {
            String sql =
                    "INSERT INTO item (id, name, item_type, category) "+
                        "VALUES (?, ?, ?, ?);";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            st.setString(2, name);
            st.setString(3, item_type);
            st.setInt(4, category);
            st.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void dropTable(){
        try {
            this.statement.executeUpdate("DROP TABLE item");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static SleepItemDao getInstance() {
        return singleton;
    }
}
