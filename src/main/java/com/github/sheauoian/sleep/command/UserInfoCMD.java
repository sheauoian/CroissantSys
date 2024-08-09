package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.DbDriver;
import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import com.github.sheauoian.sleep.player.UserInfo;
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
            "strength",
            "defence",
            "max_health",
            "info",
            "manager",
            "drop"
    };
    public UserInfoCMD(Sleep plugin) {
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
                try {
                    ResultSet rs = DbDriver.singleton().getStatement().executeQuery("select uuid from user;");
                    while (rs.next()) {
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        OfflinePlayer result = getOfflinePlayer(uuid);
                        sender.sendMessage(Objects.requireNonNull(result.getName()));
                    }
                } catch(SQLException e) {
                    Sleep.logger.info(e.getMessage());
                }
            }

            // strength
            else if (args[0].equals("strength")) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float strength = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setStrength(strength);
                }
            }

            // defence
            else if (args[0].equals("defence")) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float defence = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setDefence(defence);
                }
            }

            // max_health
            else if (args[0].equals("max_health")) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float max_health = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setMaxHealth(max_health);
                }
            }

            // info - show info
            else if (args[0].equals("info")) {
                UserInfo info;
                if (args.length >= 2)
                    info = Sleep.userManager.getInfo(args[1]);
                else if (sender instanceof Player p)
                    info = Sleep.userManager.getInfo(p.getUniqueId().toString());
                else return false;
                if (info != null) {
                    sender.sendMessage(String.format("存在しました: %s", info.uuid));
                } else {
                    sender.sendMessage("That account is not exist");
                }
            }

            // manager
            else if (args[0].equals(completeList[5])) {
                for (UserInfo info : Sleep.userManager.getLoadedUsers()) {
                    sender.sendMessage(info.uuid);
                }
            }

            // drop
            else if (args[0].equals("drop")) {
                UserInfoDao.getInstance().dropTable();
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
            if (args[0].equals(completeList[4])) {
                try {
                    List<String> uuids = new ArrayList<>();
                    ResultSet resultSet = DbDriver.singleton().getStatement().executeQuery("select uuid from user");
                    while(resultSet.next()) {
                        uuids.add(resultSet.getString("uuid"));
                    }
                    return uuids;
                } catch (SQLException e) {
                    Sleep.logger.info(e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    CMD getInstance() { return this; }
    @Override
    public String getCommandName() { return "user"; }
}
