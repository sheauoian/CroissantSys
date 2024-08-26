package com.github.sheauoian.croissantsys.command;

import com.github.sheauoian.croissantsys.CroissantSys;
import com.github.sheauoian.croissantsys.common.item.ServerItem;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentDao;
import com.github.sheauoian.croissantsys.common.item.ServerItemDao;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentInfo;
import net.kyori.adventure.text.Component;
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
import java.util.Objects;

public class ItemCMD extends CMD implements TabCompleter {
    private final String[] completeList = new String[] { "list", "register", "rename", "get", "drop", "create"};
    public ItemCMD(CroissantSys plugin) {
        super(plugin);
    }
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String text,
            @NotNull String[] args)
    {
        ServerItemDao dao = ServerItemDao.getInstance();
        if (args.length >= 1) {
            if (args[0].equals("list")) {
                for (ServerItem item : dao.getAll()) {
                    sender.sendMessage(item.toString());
                }
            }
            else if (args[0].equals("register")) {
                if (
                        args.length >= 2 &&
                        sender instanceof Player p &&
                        !p.getInventory().getItemInMainHand().getType().isAir()
                ) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    String displayName_json =
                            JSONComponentSerializer.json().serialize(Objects.requireNonNullElse(
                                    item.getItemMeta().displayName(),
                                    Component.text("Unnamed Item")));
                    p.sendMessage(displayName_json);
                    dao.insert(
                            args[1],
                            displayName_json,
                            item.getType().toString(),
                            0);
                }
            }
            if (args[0].equals("rename")) {
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
            else if (args[0].equals("get")) {
                if (
                        args.length >= 2 &&
                        sender instanceof Player p
                ) {
                    ServerItem serverItem = dao.get(args[1]);
                    if (serverItem != null) {
                        p.getInventory().addItem(serverItem.getItemStack());
                    }
                }
            } else if (args[0].equals("drop")) {
                dao.dropTable();
                dao.createTable();
            } else if (args[0].equals("create")) {
                if (sender instanceof Player p && args.length >= 2) {
                    EquipmentInfo info = EquipmentInfo.create(args[1]);
                    info.save();
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
