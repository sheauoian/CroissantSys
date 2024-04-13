package com.github.sheauoian.sleep.item;

import com.github.sheauoian.sleep.DbDriver;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.github.sheauoian.sleep.Sleep.logger;

public class StorageItem extends CollectableItem{
    public final UUID uuid;
    public final String item_id;
    public final Material material;

    public StorageItem(UUID uuid, String item_id, int amount) {
        super(amount);
        this.uuid = uuid;
        this.item_id = item_id;
        Material material = Material.AIR;
        try {
            PreparedStatement st = DbDriver.singleton().getConnection().prepareStatement("""
                    SELECT item_type FROM item WHERE id = ? LIMIT 1
                    """);
            st.setString(1, item_id);
            ResultSet resultSet = st.executeQuery();
            material = Material.getMaterial(resultSet.getString("item_type"));
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        this.material = material;
    }
    @Override
    public String toString() {
        return String.format("('%s', '%s', %s)",uuid, item_id, amount);
    }
}
