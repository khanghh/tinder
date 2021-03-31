package com.example.rest.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SigninService {

    @POST("/api/login")
    Call<SigninResponse> login(@Body SignBody body);

    class SigninResponse {
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("authToken")
        @Expose
        private String authToken;
        @SerializedName("user")
        @Expose
        private User user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    class User {

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
        private String phone;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("workplace")
        @Expose
        private String workplace;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("swipe_gender")
        @Expose
        private String swipe_gender;
        @SerializedName("min_age")
        @Expose
        private Integer min_age;
        @SerializedName("max_age")
        @Expose
        private Integer max_age;
        @SerializedName("max_distance")
        @Expose
        private Integer max_distance;

        public String getSwipe_gender() {
            return swipe_gender;
        }

        public void setSwipe_gender(String swipe_gender) {
            this.swipe_gender = swipe_gender;
        }

        public Integer getMin_age() {
            return min_age;
        }

        public void setMin_age(Integer min_age) {
            this.min_age = min_age;
        }

        public Integer getMax_age() {
            return max_age;
        }

        public void setMax_age(Integer max_age) {
            this.max_age = max_age;
        }

        public Integer getMax_distance() {
            return max_distance;
        }

        public void setMax_distance(Integer max_distance) {
            this.max_distance = max_distance;
        }

        public String getWorkplace() {
            return workplace;
        }

        public void setWorkplace(String workplace) {
            this.workplace = workplace;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    class SignBody {
        public SignBody(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
