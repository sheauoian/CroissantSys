package com.github.sheauoian.croissantsys.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringDate {
    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
    }
}
