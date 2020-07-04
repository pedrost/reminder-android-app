package com.example.todoapp.ui.todo;

import androidx.annotation.Nullable;

public class TodoFormState {
    @Nullable
    private Integer todoNameError;
    @Nullable
    private Integer todoDateError;
    private boolean isDataValid;

    TodoFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.todoNameError = usernameError;
        this.todoDateError = passwordError;
        this.isDataValid = false;
    }

    TodoFormState(boolean isDataValid) {
        this.todoNameError = null;
        this.todoDateError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getTodoNameError() {
        return todoNameError;
    }

    @Nullable
    Integer getTodoDateError() {
        return todoDateError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
