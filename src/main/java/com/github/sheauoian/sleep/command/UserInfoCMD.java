package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.DbDriver;
import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.player.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserInfoCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] { "list", "strength", "defence", "max_health" };
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
        SleepItemDao dao = SleepItemDao.getInstance();
        if (args.length >= 1) {
            if (args[0].equals(completeList[0])) {
                try {
                    ResultSet rs = DbDriver.singleton()
                            .getStatement().executeQuery("select uuid from user;");
                    while (rs.next()) {
                        @Nullable
                        String mcid = Bukkit.getOfflinePlayer(rs.getString("uuid")).getName();
                        sender.sendMessage(String.format("%s: %s",
                                rs.getString("uuid"),
                                Objects.requireNonNullElse(mcid, "不明のプレイヤー")));
                    }
                } catch(SQLException e) {
                    Sleep.logger.info(e.getMessage());
                }
            } else if (args[0].equals(completeList[1])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float strength = Float.parseFloat(args[1]);
                    UserManager.getInstance().get(p).setStrength(strength);
                }
            } else if (args[0].equals(completeList[2])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float defence = Float.parseFloat(args[1]);
                    UserManager.getInstance().get(p).setDefence(defence);
                }
            } else if (args[0].equals(completeList[3])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    float max_health = Float.parseFloat(args[1]);
                    UserManager.getInstance().get(p).setMaxHealth(max_health);
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
        }
        return null;
    }

    @Override
    CMD getInstance() {
        return this;
    }
    @Override
    public String getCommandName() {
        return "user";
    }
}
