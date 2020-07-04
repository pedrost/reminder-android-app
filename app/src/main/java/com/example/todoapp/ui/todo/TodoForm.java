package com.example.todoapp.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;
import com.example.todoapp.data.model.CurrentUser;
import com.example.todoapp.data.model.Todo;

public class TodoForm extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private EditText todoInputName;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);
        loadingProgressBar = findViewById(R.id.todoLoading);

        if(dbHelper == null) {
            dbHelper = new DBHelper(this);
        }


    }

    public void createTodo(View view) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        todoInputName = findViewById(R.id.todoInputName);
        dbHelper.insertTodo(new Todo(
                CurrentUser.getInstance().getUserId(),
                todoInputName.getText().toString()
        ));
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    todoInputName.setText("");
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            },
        3000);
    }

}
