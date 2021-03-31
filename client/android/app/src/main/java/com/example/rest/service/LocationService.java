package com.example.rest.service;

import com.example.rest.model.MessageError;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LocationService {

    @POST("/api/location")
    Call<MessageError> updateLocation(@Body LocationRequestBody body, @Header("Authorization") String token);

    class LocationRequestBody {
        public LocationRequestBody() {

        }

        public LocationRequestBody(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

    }
}