package com.example.todoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoApp";
    private static final String USERS_TABLE_NAME = "users";
    private static final String TODOS_TABLE_NAME = "todos";

    private static final String USERS_TABLE_CREATE = "create table users (" +
            "userId integer primary key not null, " +
            "email text not null," +
            "password text not null," +
            "avatar text not null," +
            "displayName text not null )";

    private static final String TODOS_TABLE_CREATE = "create table todos ( " +
            "todoId integer primary key not null, " +
            "userId integer not null," +
            "todoName text not null," +
            "done boolean not null," +
            "FOREIGN KEY (userId) REFERENCES " + USERS_TABLE_NAME + " (userId) )";

    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE_CREATE);
        db.execSQL(TODOS_TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String queryUSERS = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME;
        String queryTODOS = "DROP TABLE IF EXISTS " + TODOS_TABLE_NAME;
        db.execSQL(queryUSERS);
        db.execSQL(queryTODOS);
        this.onCreate(db);
    }
}
