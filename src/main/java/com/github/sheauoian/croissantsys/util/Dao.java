package com.github.sheauoian.croissantsys.util;

import com.github.sheauoian.croissantsys.DbDriver;

import java.sql.Connection;

public interface Dao {
    Connection con = DbDriver.getInstance().getConnection();
    void init();
}
