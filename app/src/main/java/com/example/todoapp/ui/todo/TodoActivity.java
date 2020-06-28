package com.example.todoapp.ui.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.appcompat.app.AppCompatActivity;


import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private List<String> toDoList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private EditText todoInputName;
    private SparseBooleanArray sparseBooleanArray;


    public TodoActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

         todoInputName = findViewById(R.id.todoNameInput);

        if(dbHelper == null) {
            dbHelper = new DBHelper(this);
        }

        toDoList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, toDoList);
        listView = findViewById(R.id.todoList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toDoList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }, 300);
            }
        });

    }

    public void createTodo(View view) {
        Log.println(Log.ERROR, "criar", "tentando criar");
        toDoList.add(todoInputName.getText().toString());
        arrayAdapter.notifyDataSetChanged();
        todoInputName.setText("");
    }

}
