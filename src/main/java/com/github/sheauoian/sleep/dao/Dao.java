package com.github.sheauoian.sleep.dao;

import com.github.sheauoian.sleep.DbDriver;
import com.github.sheauoian.sleep.Sleep;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class Dao {
    protected final Connection connection = DbDriver.singleton().getConnection();
    protected final Statement statement = DbDriver.singleton().getStatement();
    protected final Logger logger = Sleep.logger;

    public abstract void createTable();
}
