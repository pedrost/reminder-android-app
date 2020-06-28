package com.example.todoapp.ui.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;

public class TodoActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;

    public TodoActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        if(dbHelper == null) {
            dbHelper = new DBHelper(this);
        }
        Cursor cursor = dbHelper.getAllUsers();
        final TextView textView = findViewById(R.id.allUsersFromDb);
        if(cursor.moveToFirst()) {
            do {
                textView.append("Email: " + cursor.getString(cursor.getColumnIndex("email")));
            } while (cursor.moveToNext());
        }
    }

}
