package com.example.todoapp.ui.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.todoapp.ui.login.LoginActivity;
import com.example.todoapp.ui.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private List<String> toDoList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private SparseBooleanArray sparseBooleanArray;
    private Context context;


    public TodoActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        context = this;

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
                String dataTodo = cursor.getString(cursor.getColumnIndex("todoDate"));
                String horaTodo = cursor.getString(cursor.getColumnIndex("todoHour"));
                toDoList.add(nomeTodo + " " + dataTodo + " " + horaTodo);
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
                        String[] separated = toDoList.get(position).split(" ");
                        dbHelper.deleteTodoByName(separated[0]);
                        toDoList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }, 300);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.goToProfileButton) {
            goToProfile();
            return true;
        }

        if(id == R.id.logoutButton) {
            goToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void goToTodoForm(View view) {
        Intent intent = new Intent(context, TodoForm.class);
        startActivity(intent);
    }

    public void goToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    public void goToProfile() {
        Intent intent = new Intent(context, ProfileActivity.class);
        startActivity(intent);
    }

}
