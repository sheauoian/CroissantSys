package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import com.github.sheauoian.sleep.item.SleepItem;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ItemCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] { "list", "register", "rename", "get" };
    public ItemCMD(Sleep plugin) {
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
                for (SleepItem item : dao.getAll()) {
                    sender.sendMessage(item.toString());
                }
            }
            else if (args[0].equals(completeList[1])) {
                if (
                        args.length >= 2 &&
                        sender instanceof Player p &&
                        !p.getInventory().getItemInMainHand().getType().isAir()
                ) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    String displayName_json =
                            JSONComponentSerializer.json().serialize( item.displayName()
                                    .decoration(TextDecoration.ITALIC, false)
                            );
                    p.sendMessage(displayName_json);
                    dao.insert(
                            args[1],
                            displayName_json,
                            item.getType().toString(),
                            0);
                }
            }
            if (args[0].equals(completeList[2])) {
                if (
                        args.length >= 2 &&
                        sender instanceof Player p &&
                        !p.getInventory().getItemInMainHand().getType().isAir()
                ) {
                    String new_name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    Component displayName = LegacyComponentSerializer.legacyAmpersand().deserialize(new_name)
                            .decoration(TextDecoration.ITALIC, false);
                    ItemStack new_item = p.getInventory().getItemInMainHand();
                    ItemMeta meta = new_item.getItemMeta();
                    meta.displayName(displayName);
                    new_item.setItemMeta(meta);
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new_item);
                }
            }
            else if (args[0].equals(completeList[3])) {
                if (
                        args.length >= 2 &&
                        sender instanceof Player p
                ) {
                    SleepItem sleepItem = dao.getByID(args[1]);
                    if (sleepItem != null) {
                        p.getInventory().addItem(sleepItem.getItemStack());
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
        return "item";
    }
}
