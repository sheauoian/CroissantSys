package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.item.SleepItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SleepCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] { "version", "help", "くたばってしまえ", "get" };
    public SleepCMD(Sleep plugin) {
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
            if (args[0].equals(completeList[0])) {
                sender.sendMessage("Sleep 1.0");
            }
            else if (args[0].equals(completeList[1])) {
                sender.sendMessage("/sleep version : バージョンを表示します");
                sender.sendMessage("/sleep help : ヘルプを表示します");
            }
            else if (args[0].equals(completeList[2])) {
                sender.sendMessage("くたばってしまいもうしわけありませんでしta");
                if (args.length >= 3) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        Material material = p.getItemOnCursor().getType();
                        new SleepItem(args[1], args[2], material.toString(), 0).save();
                    }
                }
            }
            else if (args[0].equals(completeList[3])) {
                if (args.length >= 2) {
                    if (Sleep.sleepItemDAO.getByID(args[1]) != null) {
                        sender.sendMessage("存在するのさ");
                    } else {
                        sender.sendMessage("存在しないのさ");
                    }
                } else {
                    List<SleepItem> sleepItems = Sleep.sleepItemDAO.getAll();
                    if (sender instanceof Player p) {
                        for(SleepItem i : sleepItems) {
                            p.sendMessage(i.toString());
                            p.getInventory().addItem(i.getItemStack());
                        }
                    } else {
                        for(SleepItem i : sleepItems) {
                            sender.sendMessage(i.toString());
                        }
                    }
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
        return "sleep";
    }
}
