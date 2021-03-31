package com.example.rest.service;

import com.example.rest.model.MessageError;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UpdateUserService {

    @POST("/api/settings")
    Call<MessageError> updateUserInfo (@Header("Authorization") String authorization, @Body UpdateInfoBody body);

    @POST("/api/settings")
    Call<MessageError> updateUserSettings (@Header("Authorization") String authorization, @Body UpdateSettingBody body);

    class UpdateInfoBody {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("workplace")
        @Expose
        private String workplace;

        public UpdateInfoBody (String name, String gender, int age, String phone, String description, String city, String workplace) {
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.phone = phone;
            this.description = description;
            this.city = city;
            this.workplace = workplace;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getWorkplace() {
            return workplace;
        }

        public void setWorkplace(String workplace) {
            this.workplace = workplace;
        }
    }

    class UpdateSettingBody {
        @SerializedName("swipe_gender")
        @Expose
        private String swipeGender;
        @SerializedName("min_age")
        @Expose
        private Integer minAge;
        @SerializedName("max_age")
        @Expose
        private Integer maxAge;
        @SerializedName("max_distance")
        @Expose
        private Integer maxDistance;

        public UpdateSettingBody(String swipe_gender, int min_age, int max_age, int max_distance) {
            this.swipeGender = swipe_gender;
            this.minAge = min_age;
            this.maxAge = max_age;
            this.maxDistance = max_distance;
        }

        public String getSwipeGender() {
            return swipeGender;
        }

        public void setSwipeGender(String swipeGender) {
            this.swipeGender = swipeGender;
        }

        public Integer getMinAge() {
            return minAge;
        }

        public void setMinAge(Integer minAge) {
            this.minAge = minAge;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }

        public Integer getMaxDistance() {
            return maxDistance;
        }

        public void setMaxDistance(Integer maxDistance) {
            this.maxDistance = maxDistance;
        }
    }

}
