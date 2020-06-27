package com.example.todoapp.ui.todo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;

public class TodoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper helper = new DBHelper(this);
        setContentView(R.layout.activity_todo);
    }

}
