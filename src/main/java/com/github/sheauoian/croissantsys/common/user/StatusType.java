package com.github.sheauoian.croissantsys.common.user;

public enum StatusType {
    STRENGTH("攻撃力",3.8d),
    DEFENCE("防御力", 4.5d),
    MAX_HP("最大体力", 3.5d);

    private final String displayName;
    private final double subVolume;

    StatusType(String displayName, double subVolume) {
        this.displayName = displayName;
        this.subVolume = subVolume;
    }

    public double subVolume() {
        return this.subVolume;
    }

    public String displayName() {
        return this.displayName;
    }
}
