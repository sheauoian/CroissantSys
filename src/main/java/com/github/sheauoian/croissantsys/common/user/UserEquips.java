package com.github.sheauoian.croissantsys.common.user;

import com.github.sheauoian.croissantsys.common.item.equipment.Equipment;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentDao;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentType;
import com.github.sheauoian.croissantsys.util.Damage;
import com.github.sheauoian.croissantsys.util.DamageSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserEquips implements DamageSystem {
    private final Map<EquipmentType, Equipment> EQUIPS = new HashMap<>();
    private final UserInfo info;

    public UserEquips(UserInfo info) {
        this.info = info;
        File f = new File("db/user/"+info.uuid+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        for (EquipmentType type : EquipmentType.values()) {
            if (config.contains("equips."+type.toString())) {
                int id = config.getInt("equips."+ type);
                if (id >= 0) EQUIPS.put(type, EquipmentDao.getInstance().get(id));
            }
        }
    }

    public void attach(Equipment e) {
        if (!info.uuid.equals(e.getUuid())) return;
        File f = new File("db/user/"+info.uuid+".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        try {
            if (EQUIPS.containsKey(e.getInfo().getType())) {
                EQUIPS.remove(e.getInfo().getType());
                config.set("equips."+e.getInfo().getType().toString(), -1);
            }
            else {
                EQUIPS.put(e.getInfo().getType(), e);
                config.set("equips."+e.getInfo().getType().toString(), e.getId());
            }
            config.save(f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        info.update();
    }

    // 攻撃力上昇
    // 攻撃力上昇%
    // 防御力上昇
    // 防御力上昇%
    // 体力上昇
    // 体力上昇%
    // 移動速度上昇%
    // クリティカル確率上昇%
    // クリティカルダメージ倍率上昇%
    // 自然回復頻度短縮s
    // 自然回復倍率上昇%
    // マナ効率上昇%
    // マナ上限上昇%

    // 攻撃力上昇効果%
    // 防御力上昇効果%
    // 移動速度上昇効果%
    // クリティカル確率上昇効果%
    // クリティカルダメージ倍率上昇効果%
    // 自然回復頻度上昇効果%
    // 自然回復倍率上昇効果%
    // マナ効率上昇効果%

    // 攻撃力減少効果%
    // 防御力減少効果%
    // 自然回復頻度減少効果%
    // 自然回復倍率減少効果%
    // マナ効率減少効果%


    @Override
    public void getReceiveDamage(Damage d) {
        for (Equipment equipment : EQUIPS.values())
            if (equipment != null) equipment.getReceiveDamage(d);
    }

    @Override
    public void getInflictDamage(Damage d) {
        for (Equipment equipment : EQUIPS.values())
            if (equipment != null) equipment.getInflictDamage(d);
    }

    public void calcStatus(UserInfo user) {
        for (Equipment equipment : EQUIPS.values()) {
            if (equipment == null) continue;
            for (Status status : equipment.getStatus())
                if (status != null)
                    user.addStatus(status);
        }
    }
}
