package com.example.rest;


import android.net.Uri;

import com.example.rest.service.ConversationService;
import com.example.rest.service.MessageService;
import com.example.rest.service.DeleteImageService;
import com.example.rest.service.PostImageService;
import com.example.rest.service.LocationService;
import com.example.rest.service.SearchFriendService;
import com.example.rest.service.SigninService;
import com.example.rest.service.SignupService;
import com.example.rest.service.UpdateUserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://167.99.69.92";

    private static Retrofit getNewInstance(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static SignupService getSignupService() {
        return getNewInstance(BASE_URL).create(SignupService.class);
    }

    public static SigninService getSigninService() {
        return getNewInstance(BASE_URL).create(SigninService.class);
    }

    public static MessageService getMessageService() {
        return getNewInstance(BASE_URL).create(MessageService.class);
    }


    public static PostImageService getPostImageService() {
        return getNewInstance(BASE_URL).create(PostImageService.class);
    }

    public static SearchFriendService getSearchFriendService() {
        return getNewInstance(BASE_URL).create(SearchFriendService.class);
    }

    public static DeleteImageService getDeleteImageService() {
        return getNewInstance(BASE_URL).create(DeleteImageService.class);
    }

    public static LocationService getLocationService () {
        return getNewInstance(BASE_URL).create(LocationService.class);
    }

    public static UpdateUserService getUpdateUserService() {
        return getNewInstance(BASE_URL).create(UpdateUserService.class);
    }

    public static ConversationService getConversationService() {
        return getNewInstance(BASE_URL).create(ConversationService.class);
    }
}
