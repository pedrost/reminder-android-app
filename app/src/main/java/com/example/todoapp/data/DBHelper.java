package com.example.todoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todoapp.data.model.LoggedInUser;
import com.example.todoapp.data.model.Todo;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoApp1v1";
    private static final String USERS_TABLE_NAME = "users";
    private static final String TODOS_TABLE_NAME = "todos";

    private static final String USERS_TABLE_CREATE = "create table users (" +
            "userId integer primary key not null, " +
            "email text not null," +
            "password text not null," +
            "avatar text," +
            "displayName text not null )";

    private static final String TODOS_TABLE_CREATE = "create table todos ( " +
            "todoId integer primary key not null, " +
            "userId integer not null," +
            "todoName text not null," +
            "done boolean," +
            "todoHour text," +
            "todoDate text," +
            "FOREIGN KEY (userId) REFERENCES " + USERS_TABLE_NAME + " (userId) )";

    SQLiteDatabase db = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE_CREATE);
        db.execSQL(TODOS_TABLE_CREATE);
        if(this.db == null) {
            this.db = db;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String queryUSERS = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME;
        String queryTODOS = "DROP TABLE IF EXISTS " + TODOS_TABLE_NAME;
        db.execSQL(queryUSERS);
        db.execSQL(queryTODOS);
        this.onCreate(db);
    }

    public Cursor getAllUsers() {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        return cursor;
    }

    public Cursor getAllTodosFromUser(String userId) {
        db = this.getReadableDatabase();
        Cursor a = db.rawQuery("SELECT * FROM todos WHERE userId = '" + userId + "'", null);
        return a;
    }

    public void insertUser(LoggedInUser user) {
        db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("email", user.getEmail());
        value.put("password", user.getPassword());
        value.put("displayName", user.getDisplayName());
        Log.println(Log.ERROR, "PUTTING USER IN DB", "PUTTING USER IN DB");
        db.insert("users", null, value);
    }

    public void insertTodo(Todo todo) {
        db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("userId", todo.getUserId());
        value.put("todoName", todo.getName());
        value.put("done", todo.getDone());
        value.put("todoDate", todo.getDate());
        value.put("todoHour", todo.getHour());
        Log.println(Log.ERROR, "PUTTING TODO IN DB", "PUTTING TODO IN DB");
        db.insert("todos", null, value);
    }

    public LoggedInUser findUserByEmail(String emailParam) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = '" + emailParam + "'", null);
        if(cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
            return new LoggedInUser(userId, displayName, email, password);
        } else {
            return new LoggedInUser("", "", "", "");
        }
    }

    public void deleteTodoByName(String name) {
        db = this.getReadableDatabase();
        String rawquery = "DELETE FROM todos WHERE todoName = '" + name + "'";
        db.execSQL(rawquery);
        Log.println(Log.ERROR, "Deleting todo", rawquery);
    }
}
