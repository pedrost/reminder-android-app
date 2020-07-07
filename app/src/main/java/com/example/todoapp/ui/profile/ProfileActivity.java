package com.example.todoapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.data.DBHelper;
import com.example.todoapp.data.ImageLoadTask;
import com.example.todoapp.data.ImageResponse;
import com.example.todoapp.data.NetworkClient;
import com.example.todoapp.data.UploadApis;
import com.example.todoapp.data.model.CurrentUser;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private Context context;
    private ImageView imageView;
    private Uri filePath;
    private Bitmap bitmap;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        imageView = findViewById(R.id.profilePic);
        if (dbHelper == null) {
            dbHelper = new DBHelper(this);
        }

        Cursor cursor = dbHelper.getUserById(CurrentUser.getInstance().getUserId());
        TextView emailValue = findViewById(R.id.userEmailValue);
        Log.e("MOVE TO FIRST", Boolean.toString(cursor.moveToFirst()));
        if (cursor.moveToFirst()) {
            do {
                String UserId = cursor.getString(cursor.getColumnIndex("userId"));
                String userEmail = cursor.getString(cursor.getColumnIndex("email"));
                String avatarLink = cursor.getString(cursor.getColumnIndex("avatar"));
                Log.e("UserData: ", UserId + " " + userEmail + " " + avatarLink);
                emailValue.setText(userEmail);
                if(avatarLink != null && !avatarLink.equals(""))
                    new ImageLoadTask(avatarLink, imageView).execute();
            } while (cursor.moveToNext()) ;
        } else {
            Log.e("ERROR : ", "Cant find user id " + CurrentUser.getInstance().getUserId() );
        }
    }

    public void openGallery(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickFromGalery();
            }
        } else {
            pickFromGalery();
        }
    }

    public void pickFromGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            filePath = data.getData();
            uploadImage(filePath);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(getRoundedShape(bitmap));
            } catch (IOException e) {
                Log.e("IOEXEP", e.toString());
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 232;
        int targetHeight = 232;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    public String getAbsolutePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ", new String[] { document_id }, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadImage(Uri filePathParam) {
        File file = new File(getAbsolutePath(filePathParam));
        Retrofit retrofit = NetworkClient.getRetrofit();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);

        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        try {
            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("BITRMAP", encoded);
            Call call = uploadApis.uploadImage(encoded, "Client-ID 822a9cad19004ac");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    String body = response.body().toString();
                    String[] match = body.split("=");
                    String link = match[29].replaceAll("\\}, success", "");
                    dbHelper.updateUserProfilePicLink(CurrentUser.getInstance().getUserId(), link);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("FAILURE", t.toString());
                }
            });
        } catch (IOException e) {
            Log.e("IOEXEP", e.toString());
        }
    }

}
