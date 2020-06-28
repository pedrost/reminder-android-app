package com.example.todoapp.data;

import com.example.todoapp.data.model.CurrentUser;
import com.example.todoapp.data.model.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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

            LoggedInUser user = dbHelper.findUserByEmail(email);

            // If not find any user, create one
            if(user.getPassword().equals("")) {
                LoggedInUser newUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                email,
                                email,
                                password
                        );
                CurrentUser.getInstance().setUserId(newUser.getUserId());
                dbHelper.insertUser(newUser);
                return new Result.Success<>(newUser);
            } else {
                // If finds a user, compare the password
                if(user.getPassword().equals(password)) {
                    CurrentUser.getInstance().setUserId(user.getUserId());
                    return new Result.Success<>(user);
                } else {
                    Result result = new Result.Error(new Exception("Erro ao logar"));
                    result.setErrorMessage("Senha incorreta");
                    return result;
                }
            }
        } catch (Exception e) {
            Result result = new Result.Error(new IOException("Erro ao logar", e));
            result.setErrorMessage("Erro ao logar");
            return result;
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
