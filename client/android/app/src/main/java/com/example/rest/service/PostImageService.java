package com.example.rest.service;



import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostImageService {
    @Multipart
    @POST("upload/upload_image")
    Call<ResponseBody> upImage(@Header("Authorization") String authorization, @Part("num") RequestBody num, @Part MultipartBody.Part image);

}
