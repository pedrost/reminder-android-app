package com.example.todoapp.data;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    @Multipart
    @POST("upload")
    Call<RequestBody> upload(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);

    @FormUrlEncoded
    @POST("/3/image")
    Call<Object> uploadImage(
        @Field("image") String image,
        @Header("Authorization") String authorization
    );
}