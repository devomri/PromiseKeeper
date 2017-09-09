package com.omri.dev.promisekeeper.DAL;

import java.lang.reflect.Type;

/**
 * Represents the single table of the application
 * (hence all the SQL queries are written here)
 */

public final class PromiseTable {
    // Prevent instantiation
    private PromiseTable() {}

    public static final String TABLE_NAME = "promise";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_BASE_TIME = "base_time";
    public static final String COLUMN_NAME_GUARD_CONTACT = "guard_contact";
    public static final String COLUMN_NAME_INTERVAL = "interval";
    public static final String COLUMN_NAME_LOCATION = "location";
    public static final String COLUMN_NAME_CALL_CONTACT = "call_contact";

    public static final String SQL_CREATE_ENTIRES
            = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_STATUS + " TEXT," +
                COLUMN_NAME_TITLE + " TEXT," +
                COLUMN_NAME_DESCRIPTION + " TEXT," +
                COLUMN_NAME_BASE_TIME + " TEXT," +
                COLUMN_NAME_GUARD_CONTACT + " TEXT," +
                COLUMN_NAME_INTERVAL + " TEXT," +
                COLUMN_NAME_LOCATION + " TEXT," +
                COLUMN_NAME_CALL_CONTACT + " TEXT" +
            ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SQL_CREATE_PROMISE =
            "INSERT INTO " + TABLE_NAME + "(" +
                    COLUMN_NAME_ID + "," +
                    COLUMN_NAME_TYPE + "," +
                    COLUMN_NAME_STATUS + "," +
                    COLUMN_NAME_TITLE + "," +
                    COLUMN_NAME_DESCRIPTION + "," +
                    COLUMN_NAME_BASE_TIME + "," +
                    COLUMN_NAME_GUARD_CONTACT + "," +
                    COLUMN_NAME_INTERVAL + "," +
                    COLUMN_NAME_LOCATION + "," +
                    COLUMN_NAME_CALL_CONTACT +
                    ") VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";

    public static String SQL_UPDATE_PROMIE_STATUS =
            "UPDATE " + TABLE_NAME +
            " SET " + COLUMN_NAME_STATUS + "='%s' " +
            "WHERE " + COLUMN_NAME_ID + "='%s'";

    public static final String SQL_GET_ALL_PROMISES =
            "SELECT * FROM " + TABLE_NAME;

    public static final String SQL_GET_PROMISE_BY_ID =
            "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME_ID + " = '%s'";

    public static final String SQL_GET_ALL_FUTURE_PROMISES =
            SQL_GET_ALL_PROMISES + " WHERE " + COLUMN_NAME_STATUS + " = '0'";
    public static final String SQL_GET_ALL_FULFILLED_PROMISES =
            SQL_GET_ALL_PROMISES + " WHERE " + COLUMN_NAME_STATUS + " = '1'";
    public static final String SQL_GET_ALL_UNFULFILLED_PROMISES =
            SQL_GET_ALL_PROMISES + " WHERE " + COLUMN_NAME_STATUS + " = '2'";

    public static final String SQL_DELETE_FUTURE_PROMISE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME_STATUS + " = '0' AND " +
                    COLUMN_NAME_ID + " = '%s'";
}
