package com.github.sheauoian.sleep;

import org.bukkit.plugin.java.JavaPlugin;

public final class Sleep extends JavaPlugin {
    public static Sleep instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("she slept ;)");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public Sleep getInstance() {
        return instance;
    }
}
