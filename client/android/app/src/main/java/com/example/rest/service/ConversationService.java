package com.example.rest.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.model.Message;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ConversationService {

    @GET("/api/conversations")
    Call<List<ConversationRespone>> getAllConversations(@Header("Authorization") String token);

    class ConversationRespone {
        @SerializedName("conversation_id")
        @Expose
        private Integer conversationId;

        @SerializedName("friend")
        @Expose
        private Friend friend;

        public Integer getConversationId() {
            return conversationId;
        }

        public void setConversationId(Integer conversationId) {
            this.conversationId = conversationId;
        }

        public Friend getFriend() {
            return friend;
        }

        public void setFriend(Friend friend) {
            this.friend = friend;
        }

    }

    class Friend {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private Object phone;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("workplace")
        @Expose
        private Object workplace;
        @SerializedName("city")
        @Expose
        private Object city;
        @SerializedName("description")
        @Expose
        private Object description;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Object getWorkplace() {
            return workplace;
        }

        public void setWorkplace(Object workplace) {
            this.workplace = workplace;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

    }

}
