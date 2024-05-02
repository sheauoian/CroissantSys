package com.github.sheauoian.sleep.warppoint;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class WarpPoint {
    private final String id;
    private final String name;
    private final Location location;
    public WarpPoint(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        setup();
    }
    public void setup() {
        String holo_id = "__warp__" + id;
        Location holo_loc = location.clone();
        holo_loc.add(0.5, 2, 0.5);
        if (DHAPI.getHologram(holo_id) != null) {
            DHAPI.removeHologram(holo_id);
        }
        DHAPI.createHologram(holo_id, holo_loc, false);
        Hologram holo = DHAPI.getHologram(holo_id);
        HologramPage page = Objects.requireNonNull(holo).getPage(0);
        DHAPI.addHologramLine(Objects.requireNonNull(holo), this.name);
        Action clickAction = new Action(ActionType.NONE, holo_id) {
            @Override
            public boolean execute(Player player) {
                player.sendMessage(String.format(
                        "ワープポイント [%s] を開放しました",
                        name
                ));
                return true;
            }
        };
        page.addAction(ClickType.LEFT, clickAction);

    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public Location getLocation() {
        return location;
    }
    public void warp(Player p) {
        Location holo_loc = location.clone();
        holo_loc.add(0.5, 0, 0.5);
        p.teleport(holo_loc);
    }

}
