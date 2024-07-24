package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SleepCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {"version", "help", "heal", "blind"};
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
            } else if (args[0].equals(completeList[2])) {
                if (sender instanceof Player p) {
                    Sleep.userManager.getOnlineUser(p).resetHealth();
                    p.sendMessage("HPをリセットしました");
                } else {
                    sender.sendMessage("このコマンドはプレイヤーのみぞつかうことができよう");
                }
            } else if (args[0].equals(completeList[3])) {
                if (sender instanceof Player p) {
                    Sleep.userManager.getOnlineUser(p).direction.fadeInOut();
                } else {
                    sender.sendMessage("このコマンドはプレイヤーのみぞつかうことができよう");
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
