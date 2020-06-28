package com.example.todoapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Todo {

    private int todoId;
    private int userId;
    private String name;
    private Boolean done;

    public Todo(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.done = false;
    }

    public int getTodoId() {
        return todoId;
    }

    public int getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public Boolean getDone() {
        return done;
    }

}

