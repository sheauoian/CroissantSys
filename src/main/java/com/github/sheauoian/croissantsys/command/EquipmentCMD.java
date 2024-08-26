package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.item.equipment.Equipment;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentDao;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentInfo;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentType;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class EquipmentCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {"info", "storage", "dao"};
    public EquipmentCMD(CroissantSys plugin) { super(plugin); }
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String text,
            @NotNull String[] args)
    {
        EquipmentDao equipmentDao = EquipmentDao.getInstance();
        if (args.length < 1) {
            return false;
        }

        if (args[0].equals("info")) {
            if (args.length < 2) {
                return false;
            }
            switch (args[1]) {
                case "create" -> {
                    if (args.length < 3) {
                        return false;
                    }
                    EquipmentInfo.create(args[2]);
                    sender.sendMessage("作成しました");
                }
                case "list" -> {
                    for (String id : EquipmentInfo.getAllIds())
                        sender.sendMessage(String.format("%s: %s", id, EquipmentInfo.get(id).getRarity().toString()));
                }
                case "show" -> {
                    if (args.length < 3) {
                        return false;
                    }
                    if (EquipmentInfo.get(args[2]) == null) {
                        sender.sendMessage("EQUIPMENT INFO("+args[2]+") CANNOT FIND");
                        return false;
                    }
                    EquipmentInfo info = EquipmentInfo.get(args[2]);
                    sender.sendMessage(String.format("""
                            ID: %s
                            RARITY: %s
                            SET TYPE: %s""",
                            args[2], info.getRarity().toString(), info.getSetType().toString()));
                    if (sender instanceof Player p) {
                        Component comp = info.getItem().getItemMeta().displayName();
                        if (comp != null) p.sendMessage(comp);
                    }
                }
                case "reload" -> EquipmentInfo.init();
            }
        } else if (args[0].equals("storage")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            OnlineUser user = UserManager.getInstance().getOnlineUser(((Player) sender).getUniqueId().toString());
            if (user == null) {
                sender.sendMessage("エラー!");
                return true;
            }

            if (args.length < 3) {
                if (args.length == 2) {
                    switch (args[1]) {
                        case "helmet", "chest", "leggings", "boots", "weapon" -> {
                            user.getMenu().equipment.changeMode(EquipmentType.valueOf(args[1].toUpperCase()));
                            user.getMenu().equipment.open();
                        }
                        case "reload" -> {
                            user.getMenu().equipment.reload();
                            user.message("&9こんにちは。リロードしましたんで、良い具合にお使いください。");
                        }
                    }
                }
                return false;
            }

            switch (args[1]) {
                case "get" -> {
                    EquipmentDao.getInstance().insert(user.info.uuid, args[2]);
                    user.message("&7作成しました");
                }
                case "level_up" -> {
                    try {
                        int id = Integer.parseInt(args[2]);
                        Equipment equipment = EquipmentDao.getInstance().get(id);
                        equipment.levelUp(1);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("EQUIPMENT ID MUST BE INTEGER");
                        return false;
                    }
                }
                case "item" -> {
                    try {
                        int id = Integer.parseInt(args[2]);
                        Equipment equipment = EquipmentDao.getInstance().get(id);
                        ((Player) sender).getInventory().addItem(equipment.getItemStack());
                    } catch (NumberFormatException e) {
                        sender.sendMessage("EQUIPMENT ID MUST BE INTEGER");
                        return false;
                    }
                }
                case "attach" -> {
                    try {
                        int id = Integer.parseInt(args[2]);
                        Equipment equipment = EquipmentDao.getInstance().get(id);
                        user.info.getEquips().attach(equipment);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("EQUIPMENT ID MUST BE INTEGER");
                        return false;
                    }
                }
            }
        } else if (args[0].equals("dao")) {
            if (args.length < 2) {
                return false;
            }
            switch (args[1]) {
                case "deleteall" -> {
                    for (String id : EquipmentDao.getInstance().getAllIds()) EquipmentDao.getInstance().delete(Integer.parseInt(id));
                }
            }
        }
        return false;
    }
    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] args)
    {
        if (args.length == 1) {
            return Arrays.asList(this.completeList);
        } else if (args[0].equals("info")) {
            if (args.length == 2) return List.of(new String[]{"list", "create", "show"});
            else if (args[1].equals("show")) {
                return EquipmentInfo.getAllIds();
            }
        } else if (args[0].equals("storage") && commandSender instanceof Player p) {
            if (args.length == 2) return List.of(new String[]{"level_up", "get", "item"});
            else if (args[1].equals("level_up") || args[1].equals("item")) {
                return EquipmentDao.getInstance().getAllIds(p.getUniqueId().toString());
            }
        }
        return null;
    }

    @Override
    CMD getInstance() {
        return this;
    }
    @Override
    public String getCommandName() {
        return "equipment";
    }
}
