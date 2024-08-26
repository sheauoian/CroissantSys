package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.DbDriver;
import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.item.equipment.Equipment;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentInfo;
import com.github.sheauoian.croissantsys.common.user.UserInfoDao;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import com.github.sheauoian.croissantsys.common.user.UserInfo;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;

public class UserInfoCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {
            "list",
            "status",
            "info",
            "manager",
            "drop",
            "equipment_test"
    };
    public UserInfoCMD(CroissantSys plugin) {
        super(plugin);
    }
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String text,
            @NotNull String[] args)
    {
        if (args.length >= 1) {
            // list - show all users
            if (args[0].equals("list")) {
                for (String uuid : UserInfoDao.getInstance().getAllUUIDs()) {
                    OfflinePlayer result = getOfflinePlayer(uuid);
                    sender.sendMessage(Objects.requireNonNull(result.getName()));
                }
            }

            // info - show info
            else if (args[0].equals("info")) {
                if (!(sender instanceof Player) && args.length < 2) return false;
                UserInfo info;
                if (args.length >= 2)
                    info = UserManager.getInstance().getInfo(args[1]);
                else {
                    Player p = (Player) sender;
                    info = UserManager.getInstance().getInfo(p.getUniqueId().toString());
                }
                if (info != null) {
                    sender.sendMessage(String.format("FOUND: %s", info.uuid));
                } else {
                    sender.sendMessage("CANNOT FOUND ACCOUNT");
                }
            }

            // manager
            else if (args[0].equals(completeList[5])) {
                for (UserInfo info : UserManager.getInstance().getLoadedUsers()) {
                    sender.sendMessage(info.uuid);
                }
            }

            // drop
            else if (args[0].equals("drop")) {
                UserInfoDao.getInstance().drop();
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
        } else if (args.length == 2) {
            if (args[0].equals("info")) {
                return UserInfoDao.getInstance().getAllUUIDs();
            } else if (args[0].equals("equipment_test")) {
                return EquipmentInfo.getAllIds();
            }
        }
        return null;
    }

    @Override
    CMD getInstance() { return this; }
    @Override
    public String getCommandName() { return "user"; }
}
