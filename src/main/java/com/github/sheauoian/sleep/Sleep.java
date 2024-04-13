package com.github.sheauoian.sleep;

import com.github.sheauoian.sleep.command.ItemCMD;
import com.github.sheauoian.sleep.command.SleepCMD;
import com.github.sheauoian.sleep.command.StorageCMD;
import com.github.sheauoian.sleep.command.UserInfoCMD;
import com.github.sheauoian.sleep.dao.item.SleepItemDao;
import com.github.sheauoian.sleep.dao.storage.StorageItemDao;
import com.github.sheauoian.sleep.dao.user.UserInfoDao;
import com.github.sheauoian.sleep.listener.EntityDamageListener;
import com.github.sheauoian.sleep.listener.PlayerJoinListener;
import com.github.sheauoian.sleep.listener.PlayerPickUpListener;
import com.github.sheauoian.sleep.player.PlayerLoop;
import com.github.sheauoian.sleep.player.UserManager;
import com.github.sheauoian.sleep.storage.Storage;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Sleep extends JavaPlugin {
    public static Sleep instance;
    public static Logger logger;
    public static UserManager userManager;
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        PluginManager manager = Bukkit.getPluginManager();
        userManager = new UserManager();
        new InventoryAPI(this).init();

        // Database - Create Tables
        UserInfoDao.getInstance().createTable();
        SleepItemDao.getInstance().createTable();
        StorageItemDao.getInstance().createTable();

        // Commands
        new SleepCMD(this);
        new ItemCMD(this);
        new UserInfoCMD(this);
        new StorageCMD(this);

        // Listeners
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new EntityDamageListener(), this);
        manager.registerEvents(new PlayerPickUpListener(), this);
        // Bukkit Runtime
        new PlayerLoop().runTaskTimer(this, 1L, 2L);
        // That's All
        logger.info("she slept ;)");
    }
    @Override
    public void onDisable() {
        userManager.close();
        DbDriver.singleton().closeConnection();
        getLogger().info("good night ;)");
    }
}
