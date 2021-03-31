package com.example.common;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.model.User;
import com.example.rest.RetrofitClient;
import com.example.rest.model.MessageError;
import com.example.rest.service.LocationService;
import com.example.tinder.authentication.UserAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLocation implements LocationListener {

    public static final int LOCATION_PERMISSION_REQUEST = 100;

    private LocationManager locationManager;
    private Location lastLocation;

    private OnLastPositionChanged onLastPositionChanged;

    public UserLocation(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setOnLastPositionChanged (OnLastPositionChanged onLastPositionChanged) {
        this.onLastPositionChanged = onLastPositionChanged;
    }

    @SuppressLint("MissingPermission")
    public void listenLocationUpdate(String providerType) {
        if (locationManager != null) {
            try {
                locationManager.requestLocationUpdates(providerType, 0, 0, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLastLocation(String providerType) {
        Location location = null;
        if (locationManager != null) {
            try {
                location = locationManager.getLastKnownLocation(providerType);
                if (lastLocation == null) {
                    lastLocation = location;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return location;
    }

    public static void updateLocationToServer(Location location) {
        if (location == null || UserAuth.getInstance().getUser() == null) {
            return;
        }
        Log.d("update location", "latitude: " + location.getLatitude() + "longitude: " + location.getLongitude());
        RetrofitClient.getLocationService()
                .updateLocation(new LocationService.LocationRequestBody(location.getLatitude(), location.getLongitude()), UserAuth.getInstance().getUser().getHeaderAuthenToken())
                .enqueue(new Callback<MessageError>() {
                    @Override
                    public void onResponse(Call<MessageError> call, Response<MessageError> response) {
                        Log.d("location update", "code " + response.code());
                    }

                    @Override
                    public void onFailure(Call<MessageError> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            updateLocationToServer(location);
            this.lastLocation = location;
            if (onLastPositionChanged != null) {
                onLastPositionChanged.onPositionChanged(location);
            }
        } else {
            Log.e("Location", "Location changed but can't get it");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface OnLastPositionChanged {
        void onPositionChanged(Location lastLocation);
    }
}
