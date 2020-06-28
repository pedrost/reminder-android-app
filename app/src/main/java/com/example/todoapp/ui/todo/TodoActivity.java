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
import com.example.todoapp.data.model.CurrentUser;
import com.example.todoapp.data.model.Todo;

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

        Cursor cursor = dbHelper.getAllTodosFromUser(CurrentUser.getInstance().getUserId());
        if (cursor.moveToFirst()) {
            do {
                String nomeTodo = cursor.getString(cursor.getColumnIndex("todoName"));
                toDoList.add(nomeTodo);
            } while (cursor.moveToNext()) ;
        }

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.println(Log.ERROR, "Deleting todo", toDoList.get(position).toString());
                        dbHelper.deleteTodoByName(toDoList.get(position).toString());
                        toDoList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }, 300);
            }
        });

    }

    public void createTodo(View view) {
        dbHelper.insertTodo(new Todo(
                CurrentUser.getInstance().getUserId(),
                todoInputName.getText().toString()
        ));
        toDoList.add(todoInputName.getText().toString());
        arrayAdapter.notifyDataSetChanged();
        todoInputName.setText("");
    }

}
