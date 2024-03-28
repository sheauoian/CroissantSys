package com.github.sheauoian.sleep;

import com.github.sheauoian.sleep.command.SleepCMD;
import com.github.sheauoian.sleep.item.SleepItemDAO;
import com.github.sheauoian.sleep.listener.EntityDamageListener;
import com.github.sheauoian.sleep.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;

public final class Sleep extends JavaPlugin {
    public static Sleep instance;

    public static SleepItemDAO sleepItemDAO;

    @Override
    public void onEnable() {
        instance = this;
        sleepItemDAO = new SleepItemDAO();
        sleepItemDAO.createTable();
        new SleepCMD(this);
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new EntityDamageListener(), this);
        getLogger().info("she slept ;)");
    }
    @Override
    public void onDisable() {
        sleepItemDAO.closeConnection();
        getLogger().info("good night ;)");
    }
    public Sleep getInstance() {
        return instance;
    }
}
