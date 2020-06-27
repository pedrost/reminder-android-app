package com.example.todoapp.data;

import com.example.todoapp.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private DBHelper dbHelper;

    public void setDBHelper(DBHelper helper) {
        this.dbHelper = helper;
    }

    public Result<LoggedInUser> login(String email, String password) {
        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            email,
                            email,
                            password
                            );
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
