package com.github.sheauoian.croissantsys.common.user;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Status {
    public static Status generate() {
        StatusType[] subStatus = StatusType.values();
        int i = new Random().nextInt(subStatus.length);
        StatusType type = subStatus[i];
        double volume = type.subVolume() + new Random().nextInt(4)/4d*3.5d;
        return new Status(type, volume);
    }

    private final StatusType type;
    private double volume;

    public Status(StatusType type, double volume) {
        this.type = type;
        this.volume = volume;
    }

    public StatusType type() {
        return type;
    }

    public double volume() {
        return volume;
    }
    public void volumeUp() {
        this.volume += 3.5d * new Random().nextInt(4)/4d;
    }
}
