package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class StorageCMD extends CMD {
    public StorageCMD(Sleep plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String text,
            @NotNull String[] args)
    {
        if (sender instanceof Player p) {
            Sleep.userManager.getOnlineUser(p).storage.open();
        }
        return false;
    }
    @Override
    CMD getInstance() {
        return this;
    }
    @Override
    public String getCommandName() {
        return "storage";
    }
}
