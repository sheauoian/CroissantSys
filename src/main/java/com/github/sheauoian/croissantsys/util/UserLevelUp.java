package com.github.sheauoian.croissantsys.util;

public class UserLevelUp {
    public static float getRequiredXp(int l) {
        return (l*l*2)+(l*5)+5;
    }
}
