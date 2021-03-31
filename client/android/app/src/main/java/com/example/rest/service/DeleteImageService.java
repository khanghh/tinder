package com.example.rest.service;


import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DeleteImageService {
    @POST("/upload/delete_image")
    Call<ResponseBody> deleteImage(@Header("Authorization") String authorization, @Query("num") int num);
}
