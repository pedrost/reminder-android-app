package com.example.todoapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Todo {

    private String todoId;
    private String userId;
    private String name;
    private String date;
    private String hour;
    private Boolean done;

    public Todo(String userId, String name, String date, String hour) {
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.done = false;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getDate() { return date; }

    public String getHour() { return hour; }

    public String getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public Boolean getDone() {
        return done;
    }

}

