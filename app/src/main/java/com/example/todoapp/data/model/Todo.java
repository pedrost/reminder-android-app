package com.example.todoapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Todo {

    private String todoId;
    private String userId;
    private String name;
    private Boolean done;

    public Todo(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.done = false;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public Boolean getDone() {
        return done;
    }

}

