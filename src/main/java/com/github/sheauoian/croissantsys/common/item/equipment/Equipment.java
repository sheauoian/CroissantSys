package com.github.sheauoian.croissantsys.common.item.equipment;

import com.github.sheauoian.croissantsys.common.user.Status;
import com.github.sheauoian.croissantsys.common.user.StatusType;
import com.github.sheauoian.croissantsys.util.Damage;
import com.github.sheauoian.croissantsys.util.DamageSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Equipment implements DamageSystem {
    private final int id;
    private final String uuid;
    private final String equipment_id;
    private final EquipmentInfo equipmentInfo;
    private int level;
    private final Status[] subStatus;

    public Equipment(int id, String uuid, String equipment_id, int level) {
        this.id = id;
        this.uuid = uuid;
        this.equipment_id = equipment_id;
        this.equipmentInfo = EquipmentInfo.get(equipment_id);
        this.level = level;
        int s = equipmentInfo.getRarity().getStatusSlotSize();
        this.subStatus = new Status[s];

        File f = new File("db/storage/equipment/"+uuid+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);

        // Sub Status
        for (int i = 0; i < s; i++) {
            if (config.contains(id +".sub_status."+ i +".")) {
                StatusType type = StatusType.valueOf(config.getString(id +".sub_status."+ i +".type"));
                double volume = config.getDouble(id +".sub_status."+ i +".volume");
                this.subStatus[i] = new Status(type, volume);
            }
        }
    }

    // Getter
    public int getId() {return id;}
    public String getUuid() {return uuid;}
    public int getLevel() {return level;}
    public String getItemId() {return equipment_id;}
    public EquipmentInfo getInfo() {return equipmentInfo;}

    // Setter
    public boolean levelUp(int l) {
        if (level >= 20) return false;
        for (int i = 0; i < l; i++) {
            level += 1;
            if (level >= 20) return true;
            else if (level % 4 == 0) statusUp();
        }
        save();
        return true;
    }

    public void statusUp() {
        int s = equipmentInfo.getRarity().getStatusSlotSize();
        for (int i = 0; i < s; i++) {
            if (this.subStatus[i] == null) {
                Status status = Status.generate();
                subStatus[i] = status;
                return;
            }
        }
        int key = new Random().nextInt(s);
        this.subStatus[key].volumeUp();
    }

    public void save() {
        EquipmentDao.getInstance().save(this);
        File f = new File("db/storage/equipment/"+uuid+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);

        int s = equipmentInfo.getRarity().getStatusSlotSize();
        for (int i = 0; i < s; i++) {
            if (subStatus[i] != null) {
                config.set(id +".sub_status."+ i +".type", subStatus[i].type().toString());
                config.set(id +".sub_status."+ i +".volume", subStatus[i].volume());
            }
        }
        try {
            config.save(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void getReceiveDamage(Damage d) {}

    @Override
    public void getInflictDamage(Damage d) {}

    public List<Status> getStatus() {
        List<Status> list = new ArrayList<>();
        list.add(equipmentInfo.getMainStatus());
        Collections.addAll(list, this.subStatus);
        return list;
    }

    public ItemStack getItemStack() {
        ItemStack item = getInfo().getItem();
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        if (lore == null) return item;
        lore.add(MiniMessage.miniMessage().deserialize("&8Equipment ID : "+ id));
        lore.add(MiniMessage.miniMessage().deserialize("&8UUID Of Owner : "+ uuid));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
