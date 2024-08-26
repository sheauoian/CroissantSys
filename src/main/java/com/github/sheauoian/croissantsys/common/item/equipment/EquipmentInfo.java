package com.github.sheauoian.croissantsys.common.item.equipment;

import com.github.sheauoian.croissantsys.common.user.Status;
import com.github.sheauoian.croissantsys.common.user.StatusType;
import com.github.sheauoian.croissantsys.util.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Equipment の基礎情報をYAMLに纏める
public class EquipmentInfo {
    private final String id;
    private final Component displayName;
    private final List<String> lore_4set, lore_2set;
    private final EquipmentType type;
    private final Rarity rarity;
    private final EquipmentSet set;
    private final Material item_type;
    private final Status mainStatus;

    private static final Map<String, EquipmentInfo> list = new HashMap<>();

    public static void init() {
        list.clear();
        File f = new File("db/equipments.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        for (String id : config.getKeys(false)) {
            Material m;
            String material_id = config.getString(id + ".material");
            if (material_id == null) {
                m = Material.PAPER;
            } else {
                m = Material.getMaterial(material_id);
            }
            list.put(id, new EquipmentInfo(
                    id,
                    config.getComponent(id + ".display_name", JSONComponentSerializer.json()),
                    config.getStringList(id + ".lore.4set"),
                    config.getStringList(id + ".lore.2set"),
                    EquipmentType.valueOf(config.getString(id + ".type")),
                    Rarity.valueOf(config.getString(id + ".rarity")),
                    EquipmentSet.valueOf(config.getString(id + ".set")),
                    m,
                    new Status(StatusType.valueOf(config.getString(id + ".status.main.type")), config.getInt(id + ".status.main.volume"))
            ));
        }
    }

    public static boolean contains(String id) {
        return list.containsKey(id);
    }

    public void save() {
        File f = new File("db/equipments.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        config.setComponent(id+".display_name", JSONComponentSerializer.json(), displayName);
        config.set(id+".lore.4set", lore_4set);
        config.set(id+".lore.2set", lore_2set);
        config.set(id+".type", type.toString());
        config.set(id+".rarity", rarity.toString());
        config.set(id+".set", set.toString());
        config.set(id+".material", item_type.toString());
        config.set(id+".status.main.type", mainStatus.type().toString());
        config.set(id+".status.main.volume", mainStatus.volume());
        try {
            config.save(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> getAllIds() {
        return list.keySet().stream().toList();
    }
    public static EquipmentInfo create(String equipment_id) {
        EquipmentInfo info = new EquipmentInfo(
                equipment_id,
                MiniMessage.miniMessage().deserialize("<color:#aadd66>ここに装備名を入力"),
                List.of(new String[]{"ここに", "4セット効果内容を", "入力"}),
                List.of(new String[]{"ここに", "2セット効果内容を", "入力"}),
                EquipmentType.CHEST,
                Rarity.COMMON,
                EquipmentSet.NORMAL,
                Material.DIAMOND_CHESTPLATE,
                new Status(StatusType.STRENGTH, 10));
        info.save();
        init();
        return info;
    }

    private EquipmentInfo(String equipment_id, Component displayName, List<String> lore_4set, List<String> lore_2set, EquipmentType type, Rarity rarity, EquipmentSet set, Material item_type, Status mainStatus) {
        this.id = equipment_id;
        this.displayName = displayName;
        this.lore_4set = lore_4set;
        this.lore_2set = lore_2set;
        this.type = type;
        this.rarity = rarity;
        this.set = set;
        this.item_type = item_type;
        this.mainStatus = mainStatus;
    }

    public Rarity getRarity() {
        return rarity;
    }
    public Status getMainStatus() {
        return mainStatus;
    }
    public EquipmentType getType() { return type; }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(item_type);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();


        lore.add(MiniMessage.miniMessage().deserialize("<color:#e8e8e8><b>>> 2セット:</b></color>"));
        for (String s : lore_2set) {
            Component comp = MiniMessage.miniMessage().deserialize("<color:#d8d8d8> "+s+"</color>");
            lore.add(comp);
        }
        lore.add(MiniMessage.miniMessage().deserialize("<color:#e8e8e8><b>>> 4セット:</b></color>"));
        for (String s : lore_4set) {
            Component comp = MiniMessage.miniMessage().deserialize("<color:#d8d8d8> "+s+"</color>");
            lore.add(comp);
        }

        lore.add(Component.text(""));
        lore.add(Component.text(type.name()));
        lore.add(Component.text(rarity.name()));
        lore.add(Component.text(set.name()));

        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public static EquipmentInfo get(String itemId) {
        return list.get(itemId);
    }

    public EquipmentSet getSetType() {
        return set;
    }
}
