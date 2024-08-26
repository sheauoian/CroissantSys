package com.github.sheauoian.croissantsys.common.warppoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpPointManager {
    private static final WarpPointManager instance = new WarpPointManager();
    public static WarpPointManager getInstance() {
        return instance;
    }

    private static final Map<String, WarpPoint> warpPoints =  new HashMap<>();
    public void init() {
        for (WarpPoint point : WarpPointDao.getInstance().getAll()) warpPoints.put(point.getId(), point);
    }

    public void drop() {
        warpPoints.clear();
        WarpPointDao.getInstance().drop();
    }
    public WarpPoint get(String id) {
        if (warpPoints.containsKey(id))
            return warpPoints.get(id);
        return null;
    }
    public List<WarpPoint> getAll() {
        return warpPoints.values().stream().toList();
    }
    public void put(String id, WarpPoint warpPoint) {
        warpPoints.put(id, warpPoint);
    }
    public void close() {
        WarpPointDao.getInstance().insertAll();
    }
}
