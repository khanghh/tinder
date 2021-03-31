package com.example.tinder.authentication;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.internet_connection.SocketIO;
import com.example.model.User;
import com.example.rest.service.SigninService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserAuth implements User.OnLoginCallBack {
    public static final int UN_AUTHENTICATED = -1;
    public static final int AUTHENTICATED = 1;
    public static final int INVALID_AUTHEN = -100;

    public static final int NONE = -5;

    private ArrayList<StateObserver> observers;
    private OnFirstAuthenListener onFirstAuthenListener;

    private int state;
    private User user;
    private SocketIO socketIO;

    private static UserAuth userAuth;

    public SocketIO getSocketIO() {
        return socketIO;
    }

    public void setSocketIO(SocketIO socketIO) {
        this.socketIO = socketIO;

    }

    public int getState() {
        return state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addStateObserver(StateObserver observer) {
        observers.add(observer);
    }

    public void setOnFirstAuthenListener(OnFirstAuthenListener onFirstAuthenListener) {
        this.onFirstAuthenListener = onFirstAuthenListener;
    }

    private UserAuth() {
        observers = new ArrayList<>();
        state = UN_AUTHENTICATED;
    }

    public static UserAuth getInstance() {
        if (userAuth == null) {
            userAuth = new UserAuth();
        }
        return userAuth;
    }

    public void setState(int state, int messageCode) {
        this.state = state;
        for (StateObserver observer : observers) {
            observer.onStateChange(state, messageCode);
        }
    }

    public boolean isAuthencated() {
        return state == AUTHENTICATED;
    }

    public void authenticate(String email, String password) throws Exception {
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        user.setOnLoginCallBack(this);
        user.login();
    }

    public void authencationWithToken(String token) {

    }

    public void refuseAuthentication() {
        setState(UN_AUTHENTICATED, NONE);
    }


    @Override
    public void onLoginSuccess(SigninService.SigninResponse response) {
        this.state = AUTHENTICATED;
        this.user = new User(response.getUser());
        this.user.setAuthen_token(response.getAuthToken());
        if (this.onFirstAuthenListener != null) {
            this.onFirstAuthenListener.onAuthenSuccess(response.getAuthToken());
        }
        // when login success, init SocketIO for using latter in chat, like, ...
        this.setSocketIO(new SocketIO("http://167.99.69.92:8889", user.getAuthen_token()));
        setState(AUTHENTICATED, NONE);
    }

    @Override
    public void onLoginFail(int error) {
        setState(INVALID_AUTHEN, error);
    }

    public interface StateObserver {
        void onStateChange(int state, int messageCode);
    }

    public interface OnFirstAuthenListener {
        void onAuthenSuccess(String authenToken);
    }
}
