package com.github.sheauoian.croissantsys;

import com.github.sheauoian.croissantsys.command.*;
import com.github.sheauoian.croissantsys.common.item.equipment.EquipmentDao;
import com.github.sheauoian.croissantsys.common.user.UserManager;
import com.github.sheauoian.croissantsys.common.item.ServerItemDao;
import com.github.sheauoian.croissantsys.common.item.StorageItemDao;
import com.github.sheauoian.croissantsys.common.user.UserInfoDao;
import com.github.sheauoian.croissantsys.common.user.UserRunnable;
import com.github.sheauoian.croissantsys.listener.EntityDamageListener;
import com.github.sheauoian.croissantsys.listener.PlayerJoinListener;
import com.github.sheauoian.croissantsys.listener.PlayerPickUpListener;
import com.github.sheauoian.croissantsys.common.warppoint.UnlockedWarpPointDao;
import com.github.sheauoian.croissantsys.common.warppoint.WarpPointManager;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CroissantSys extends JavaPlugin {
    private static CroissantSys instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        saveDefaultConfig();
        PluginManager manager = Bukkit.getPluginManager();
        new InventoryAPI(this).init();

        // Database - Create Tables
        EquipmentDao.getInstance().init();
        UserInfoDao.getInstance().init();
        ServerItemDao.getInstance().init();
        StorageItemDao.getInstance().init();
        UnlockedWarpPointDao.getInstance().init();

        // Commands
        new CroissantCMD(this);
        new ItemCMD(this);
        new UserInfoCMD(this);
        new StorageCMD(this);
        new WarpPointCMD(this);
        new WarpCMD(this);
        new EquipmentCMD(this);

        // Listeners
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new EntityDamageListener(), this);
        manager.registerEvents(new PlayerPickUpListener(), this);

        new UserRunnable().runTaskTimer(this, 1L, 2L);

        logger.info("Croissant System - Completely Loaded");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        // Equipments
        //
    }

    @Override
    public void onDisable() {
        saveConfig();
        UserManager.getInstance().close();
        WarpPointManager.getInstance().close();
        DbDriver.getInstance().close();
        getLogger().info("Croissant System - Completely Stopped");
    }

    public static CroissantSys getInstance() {
        return instance;
    }
}
