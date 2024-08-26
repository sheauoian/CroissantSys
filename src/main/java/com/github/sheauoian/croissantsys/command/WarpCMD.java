package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCMD extends CMD{
    public WarpCMD(CroissantSys plugin) {
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
            UserManager.getInstance().getOnlineUser(p).getMenu().warp.open();
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
