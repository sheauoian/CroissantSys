package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.user.OnlineUser;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CroissantCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {"help", "heal", "setspawn", "menu"};
    public CroissantCMD(CroissantSys plugin) {
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
            switch (args[0]) {
                case "help" -> {
                    sender.sendMessage("/sleep version : バージョンを表示します");
                    sender.sendMessage("/sleep help : ヘルプを表示します");
                }
                case "heal" -> {
                    if (sender instanceof Player p) {
                        UserManager.getInstance().getOnlineUser(p).resetHealth();
                        p.sendMessage("HPをリセットしました");
                    } else {
                        sender.sendMessage("このコマンドはプレイヤーのみぞつかうことができよう");
                    }
                }
                case "setspawn" -> {
                    if (sender instanceof Player p) {
                        CroissantSys plugin = CroissantSys.getInstance();
                        Location spawn = p.getWorld().getBlockAt(p.getLocation()).getLocation();
                        spawn.setYaw(p.getYaw());
                        plugin.getConfig().set("spawnpoint", spawn);
                        plugin.saveConfig();
                        p.sendMessage("ようこそ");
                    } else {
                        sender.sendMessage("このコマンドはプレイヤーのみぞつかうことができよう");
                    }
                }
                case "menu" -> {
                    if (sender instanceof Player p) {
                        OnlineUser user = UserManager.getInstance().getOnlineUser(p);
                        user.getMenu().open();
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
        return "croissant";
    }
}
