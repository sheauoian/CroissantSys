package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.DbDriver;
import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.player.UserInfo;
import org.bukkit.Bukkit;
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

public class UserInfoCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {
            "list",
            "strength",
            "defence",
            "max_health",
            "info",
            "manager"
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
            if (args[0].equals(completeList[0])) {
                try {
                    ResultSet rs = DbDriver.singleton()
                            .getStatement().executeQuery("select uuid from user;");
                    while (rs.next()) {
                        @Nullable
                        String mcid = Bukkit.getOfflinePlayer(rs.getString("uuid")).getName();
                        sender.sendMessage(
                                String.format("%s: %s",
                                rs.getString("uuid"),
                                Objects.requireNonNullElse(mcid, "*Unknown*")
                                ));
                    }
                } catch(SQLException e) {
                    Sleep.logger.info(e.getMessage());
                }
            }

            // strength
            else if (args[0].equals(completeList[1])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float strength = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setStrength(strength);
                }
            }

            // defence
            else if (args[0].equals(completeList[2])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float defence = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setDefence(defence);
                }
            }

            // max health
            else if (args[0].equals(completeList[3])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float max_health = Float.parseFloat(args[1]);
                    String uuid = p.getUniqueId().toString();
                    Sleep.userManager.getInfo(uuid).setMaxHealth(max_health);
                }
            }

            // info - show info
            else if (args[0].equals(completeList[4])) {
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
            } else if (args[0].equals(completeList[5])) {
                for (UserInfo info : Sleep.userManager.getLoadedUsers()) {
                    sender.sendMessage(info.uuid);
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
