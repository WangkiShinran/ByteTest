package com.bytedance.database;

import android.provider.BaseColumns;

public class TodoContract {
    public static final String SQL_CRATE_TODO_TABLE = "CREATE TABLE "
        + TodoNote.TABLE_NAME
        + " ("
        + TodoNote._ID
        + " INTEGER PRIMARY KEY,"
        + TodoNote.COLUMN_TODO_CONTENT
        + " TEXT,"
        + TodoNote.COLUMN_TODO_STATE
        + " INT )";
    public static final String SQL_DELETE_TODO_TABLE = "DROP TABLE IF EXITS " + TodoNote.TABLE_NAME;

    private TodoContract() {

    }

    public static class TodoNote implements BaseColumns {
        public final static String TABLE_NAME = "todolist";
        public final static String COLUMN_TODO_CONTENT = "content";
        public final static String COLUMN_TODO_STATE = "state";
    }
}
