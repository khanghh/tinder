package com.example.rest.service;

import com.example.rest.model.UserPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SignupService {


    class Nonce {

        @SerializedName("nonce")
        @Expose
        private String nonce;

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

    }

    class Message {
        @SerializedName("message")
        @Expose
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @GET("/api/get_nonce")
    Call<Nonce> getNonce();

    @POST("/api/register")
    Call<Message> register(@Body UserPojo userPojo);

}
