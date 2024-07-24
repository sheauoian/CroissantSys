package com.github.sheauoian.sleep.command;

import com.github.sheauoian.sleep.Sleep;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class CMD implements CommandExecutor {
    private static Sleep plugin;
    public CMD(Sleep plugin) {
        if (this.getPlugin() == null) this.setPlugin(plugin);
        if (this.getInstance() == null) throw new NullPointerException("Instance is null");
        if (this.getCommandName() == null) throw new NullPointerException("Command name is null");
        this.register();
    }
    final Sleep getPlugin() {
        return CMD.plugin;
    }
    final void setPlugin(Sleep instance) {
        if (instance == null)
            throw new IllegalArgumentException("Command[/sleep:<none>] の登録に失敗しました");
        CMD.plugin = instance;
    }
    public void register() {
        PluginCommand c = this.getPlugin().getCommand(this.getCommandName());
        if (c != null) {
            c.setExecutor(this.getInstance());
            if (this.getInstance() instanceof TabCompleter)
                c.setTabCompleter((TabCompleter) this.getInstance());
        }
    }
    abstract CMD getInstance();
    public abstract String getCommandName();
}
