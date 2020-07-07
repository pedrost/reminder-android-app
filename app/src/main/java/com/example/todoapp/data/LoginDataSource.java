package com.example.todoapp.data;

import android.util.Log;

import com.example.todoapp.data.model.CurrentUser;
import com.example.todoapp.data.model.LoggedInUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private DBHelper dbHelper;
    private SecretKeySpec secret;
    private LoginDataSource EncUtil;

    public void setDBHelper(DBHelper helper) {
        this.dbHelper = helper;
    }

    public static SecretKey generateKey(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    public Result<LoggedInUser> login(String email, String password) {
        try {

            LoggedInUser user = dbHelper.findUserByEmail(email);

            // If not find any user, create one
            Log.println(Log.ERROR, "finded user password", user.getPassword().toString());
            if(user.getPassword().equals("")) {
                LoggedInUser newUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                email,
                                email,
                                password
                        );

                dbHelper.insertUser(newUser);
                CurrentUser.getInstance().setUserId(newUser.getUserId());
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
            Log.println(Log.ERROR, "error ", e.toString());
            result.setErrorMessage("Erro ao logar");
            return result;
        }
    }

    public void encriptionTest() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        SecretKey secret = generateKey("mypasword");
        byte[] encript = encryptMsg("teste", secret);
        String decripted = decryptMsg(encript, secret);
    }
}
