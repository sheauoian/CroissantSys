package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCMD extends CMD{
    public WarpCMD(Sleep plugin) {
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
            Sleep.userManager.getOnlineUser(p).warp_ui.open();
        }
        return false;
    }
    @Override
    CMD getInstance() {
        return this;
    }
    @Override
    public String getCommandName() {
        return "warp";
    }
}
