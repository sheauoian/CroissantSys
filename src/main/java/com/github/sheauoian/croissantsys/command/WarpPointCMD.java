package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.warppoint.WarpPoint;
import com.github.sheauoian.croissantsys.common.warppoint.WarpPointManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WarpPointCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] {"list", "create", "reset", "tp", "drop"};
    public WarpPointCMD(CroissantSys plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String text,
            @NotNull String[] args)
    {
        WarpPointManager manager = WarpPointManager.getInstance();
        if (args.length >= 1) {
            if (args[0].equals(completeList[0])) {
                for (WarpPoint warpPoint : manager.getAll()) {
                    sender.sendMessage(warpPoint.getId() + " -> " + warpPoint.getName());
                }
            }
            else if (args[0].equals(completeList[1])) {
                if (args.length >= 3 && sender instanceof Player p) {
                    Location loc = p.getLocation();
                    loc.setX(loc.getBlockX());
                    loc.setY(loc.getBlockY());
                    loc.setZ(loc.getBlockZ());
                    manager.put(args[1], new WarpPoint(args[1], args[2], loc));
                }
            } else if (args[0].equals(completeList[2])) {
                for (WarpPoint warpPoint : manager.getAll()) {
                    warpPoint.setup();
                }
            } else if (args[0].equals(completeList[3])) {
                if (args.length >= 2 && sender instanceof Player p) {
                    if (manager.get(args[1]) != null)
                        manager.get(args[1]).warp(p);
                }
            } else if (args[0].equals(completeList[4])) {
                manager.drop();
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
        return "warp_point";
    }
}
