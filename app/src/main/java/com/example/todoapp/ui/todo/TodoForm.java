package com.example.todoapp.ui.todo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;
import com.example.todoapp.data.model.CurrentUser;
import com.example.todoapp.data.model.Todo;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodoForm extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private EditText todoInputName;
    private EditText todoInputDate;
    private EditText todoInputHour;
    private DatePickerDialog.OnDateSetListener setListener;
    private ProgressBar loadingProgressBar;
    private Context context;
    private  Calendar calendarHourGlobal = Calendar.getInstance();
    private int hourSet;
    private int daySet;
    private int monthSet;
    private int yearSet;
    private int minuteSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);
        context = this;
        loadingProgressBar = findViewById(R.id.todoLoading);
        todoInputDate = findViewById(R.id.todoInputDate);
        todoInputHour = findViewById(R.id.todoInputHour);
        if(dbHelper == null) {
            dbHelper = new DBHelper(this);
        }

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        todoInputDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if(!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }

            }
        });

        todoInputHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourSet = hourOfDay;
                                minuteSet = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, hourSet, minuteSet);
                                calendarHourGlobal.set(0, 0, 0, hourSet, minuteSet);
                                SimpleDateFormat formatShort = new SimpleDateFormat("hh:mm aa", Locale.US);
                                String parsedHour = formatShort.format(calendar.getTime());
                                todoInputHour.setText(parsedHour);
                            }
                        }, 12, 0, false
                );

                timePicker.updateTime(hourSet, minuteSet);
                if(!timePicker.isShowing()) {
                    timePicker.show();
                }
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                daySet = dayOfMonth;
                monthSet = (month+1);
                yearSet = year;
                todoInputDate.setText(date);
            }
        };

    }

    public void createTodo(View view) {
        Boolean createAlarm = false;
        try {
            Calendar currentDate = Calendar.getInstance();

            Calendar calendar = Calendar.getInstance();
            calendar.set(yearSet, monthSet, daySet, hourSet, minuteSet);

            Date inputDate = calendar.getTime();
            if(calendar.get(Calendar.DATE) == currentDate.get(Calendar.DATE)) {
                createAlarm = true;
            }
            if(inputDate.before(currentDate.getTime())) {
                Toast.makeText(getApplicationContext(), "Data não pode ser anterior a hoje", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Campo de datas é necessário.", Toast.LENGTH_SHORT).show();
            Log.e("DATE_LESS_THAN_TODAY", "Date Parsing Exception:" + e.getMessage());
            return;
        }

        loadingProgressBar.setVisibility(View.VISIBLE);
        todoInputName = findViewById(R.id.todoInputName);
        dbHelper.insertTodo(new Todo(
                CurrentUser.getInstance().getUserId(),
                todoInputName.getText().toString(),
                todoInputDate.getText().toString(),
                todoInputHour.getText().toString()
        ));

        if(createAlarm) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intentAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intentAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, todoInputName.getText().toString());
                        intentAlarm.putExtra(AlarmClock.ALARM_SEARCH_MODE_LABEL, todoInputName.getText().toString());
                        intentAlarm.putExtra(AlarmClock.EXTRA_HOUR, hourSet);
                        intentAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minuteSet);
                        startActivity(intentAlarm);
                    }
                }, 4000);
        }

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    todoInputName.setText("");
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Lembrete criado com sucesso!", Toast.LENGTH_SHORT).show();
                    goToTodoActivity();
                }
            },
        3000);
    }


    private void goToTodoActivity() {
        Intent intent = new Intent(context, TodoActivity.class);
        startActivity(intent);
    }
}
