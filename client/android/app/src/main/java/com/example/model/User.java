package com.example.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rest.RetrofitClient;
import com.example.rest.model.MessageError;
import com.example.rest.model.UserPojo;
import com.example.rest.service.SearchFriendService;
import com.example.rest.service.SigninService;
import com.example.rest.service.SignupService;
import com.example.rest.service.UpdateUserService;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class User {

    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String ID = "id";
    public static final String AUTHEN_TOKEN = "authen_token";
    public static final String MAIL = "mail";
    public static final String PASSWORD = "password";
    public static final String DESCRIPTION = "description";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_BANNED = "is_banned";
    public static final String WORK_PLACE = "workplace";
    public static final String CITY = "city";
    public static final String SWIPE_GENDER = "swipe_gender";
    public static final String MIN_AGE = "min_age";
    public static final String MAX_AGE = "max_age";
    public static final String MAX_DISTANCE = "max_distance";

    public static final int MIN_AGE_DEFAULT_VALUE = 18;
    public static final int MAX_AGE_DEFAULT_VALUE = 30;
    public static final int MAX_DISTANCE_DEFAULT_VALUE = 5000;

    public static final int INT_NULL = -100;

    private int id;
    private String authen_token;
    private String phone;
    private String mail;
    private String password;
    private String name;
    private String decription;
    private int age;
    private String gender;
    private int longtitude;
    private int latitude;
    private int max_distance;
    private int min_age;
    private int max_age;
    private boolean is_active;
    private boolean is_banned;
    private String ban_reason;
    private String exprired_ban;
    private String created_at;
    private String updated_at;
    private String workplace;
    private String city;
    private String swipe_gender;

    // call back
    private OnRegisterCallBack registerCallBack;
    private OnLoginCallBack onLoginCallBack;

    private Activity activity;

    public void setRegisterCallBack(OnRegisterCallBack registerCallBack) {
        this.registerCallBack = registerCallBack;
    }

    public void setOnLoginCallBack(OnLoginCallBack onLoginCallBack) {
        this.onLoginCallBack = onLoginCallBack;
    }

    public User() {
        id = -1;
        authen_token = "No Data";
        phone = "No Data";
        mail = "No Data";
        password = "No Data";
        name = "No Data";
        decription = "No Data";
        age = 0;
        gender = "male";
        longtitude = -1;
        latitude = -1;
        max_distance = -1;
        min_age = -1;
        max_age = -1;
        is_active = false;
        is_banned = false;
        ban_reason = "No Data";
        exprired_ban = "No Data";
        created_at = "No Data";
        updated_at = "No Data";
        workplace = "";
        city = "";
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public User(String mail, String password, String name, int age, String gender) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public User(int id, String mail, String password, String name, int age, String gender) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.id = id;
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public User(int id, String mail, String password, String name, int age, String gender, String phone, String decription) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.phone = phone;
        this.decription = decription;
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public User(int id, String mail, String password, String name, int age, String gender, String phone, String decription, String city, String workplace) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.phone = phone;
        this.decription = decription;
        this.workplace = workplace;
        this.city = city;
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public User(SigninService.User user) {
        this.name = user.getName();
        this.mail = user.getEmail();
        this.age = user.getAge();
        this.id = user.getId();
        this.phone = user.getPhone();
        this.decription = user.getDescription();
        this.gender = user.getGender();
        this.workplace = user.getWorkplace();
        this.city = user.getCity();
        this.swipe_gender = user.getSwipe_gender();
        this.min_age = user.getMin_age();
        this.max_age = user.getMax_age();
        this.max_distance = user.getMax_distance();
    }

    public User(SearchFriendService.User user) {
        this.name = user.getName();
        this.mail = user.getEmail();
        this.age = user.getAge();
        this.id = user.getId();
        this.phone = user.getPhone();
        this.decription = user.getDescription();
        this.gender = user.getGender();
        this.workplace = user.getWorkplace();
        this.city = user.getCity();
        swipe_gender = "female";
        min_age = MIN_AGE_DEFAULT_VALUE;
        max_age = MAX_AGE_DEFAULT_VALUE;
        max_distance = MAX_DISTANCE_DEFAULT_VALUE;
    }

    public static User getLocalUser(SharedPreferences sharedPreferences) {
        User user = new User();
        int id = sharedPreferences.getInt(ID, INT_NULL);
        if (id == INT_NULL) {
            return null;
        }
        user.setId(id);
        user.setMail(sharedPreferences.getString(MAIL, ""));
        user.setName(sharedPreferences.getString(NAME, ""));
        user.setAge(sharedPreferences.getInt(AGE, 0));
        user.setGender(sharedPreferences.getString(GENDER, ""));
        user.setPhone(sharedPreferences.getString(PHONE, ""));
        user.setDecription(sharedPreferences.getString(DESCRIPTION, ""));
        user.setAuthen_token(sharedPreferences.getString(AUTHEN_TOKEN, ""));
        user.setIs_active(sharedPreferences.getBoolean(IS_ACTIVE, false));
        user.setIs_banned(sharedPreferences.getBoolean(IS_BANNED, false));
        user.setCity(sharedPreferences.getString(CITY, ""));
        user.setWorkplace(sharedPreferences.getString(WORK_PLACE, ""));
        user.setSwipe_gender(sharedPreferences.getString(SWIPE_GENDER, "male"));
        user.setMin_age(sharedPreferences.getInt(MIN_AGE, MIN_AGE_DEFAULT_VALUE));
        user.setMax_age(sharedPreferences.getInt(MAX_AGE, MAX_AGE_DEFAULT_VALUE));
        user.setMax_distance(sharedPreferences.getInt(MAX_DISTANCE, MAX_DISTANCE_DEFAULT_VALUE));

        Log.d("token", user.getAuthen_token());
        return user;
    }

    public void storeToLocal(SharedPreferences.Editor editor) {
        editor.putInt(ID, this.id);
        editor.putString(MAIL, this.mail);
        editor.putString(NAME, this.name);
        editor.putInt(AGE, this.age);
        editor.putString(GENDER, this.gender);
        editor.putString(PHONE, this.phone);
        editor.putString(DESCRIPTION, this.decription);
        editor.putString(AUTHEN_TOKEN, this.authen_token);
        editor.putBoolean(IS_ACTIVE, this.is_active);
        editor.putBoolean(IS_BANNED, this.is_banned);
        editor.putString(WORK_PLACE, this.workplace);
        editor.putString(CITY, this.city);
        editor.putString(SWIPE_GENDER, this.gender.equals("male") ? "female" : "male");
        editor.putInt(MIN_AGE, this.min_age);
        editor.putInt(MAX_AGE, 	this.max_age);
        editor.putInt(MAX_DISTANCE, this.max_distance);

        editor.apply();
    }

    private void updateInfoToLocal() {
        SharedPreferences.Editor editor = activity.getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putString(NAME, this.name);
        editor.putInt(AGE, this.age);
        editor.putString(GENDER, this.gender);
        editor.putString(PHONE, this.phone);
        editor.putString(DESCRIPTION, this.decription);
        editor.putString(WORK_PLACE, this.workplace);
        editor.putString(CITY, this.city);
        editor.apply();
        Log.d("save", "saveAthenToken: ");
    }

    private void updateSettingToLocal () {
        SharedPreferences.Editor editor = activity.getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putString(SWIPE_GENDER, swipe_gender);
        editor.putInt(MIN_AGE, min_age);
        editor.putInt(MAX_AGE, max_age);
        editor.putInt(MAX_DISTANCE, max_distance);
        editor.apply();
    }

    public void register() {
        SignupService signupService = RetrofitClient.getSignupService();
        signupService.getNonce().enqueue(new Callback<SignupService.Nonce>() {
            @Override
            public void onResponse(Call<SignupService.Nonce> call, Response<SignupService.Nonce> response) {
                if (response.body() != null) {
                    String nonce = response.body().getNonce();
                    if (nonce != null) {
                        // perform sign up
                        performRegister(nonce);
                        return;
                    }
                }
                if (registerCallBack != null) {
                    registerCallBack.onRegisterFail(OnRegisterCallBack.NONCE_NULL);
                }
            }

            @Override
            public void onFailure(Call<SignupService.Nonce> call, Throwable t) {
                t.printStackTrace();
                if (registerCallBack != null) {
                    registerCallBack.onRegisterFail(OnRegisterCallBack.REQUEST_FAIL);
                }
            }
        });
    }

    private void performRegister(String nonce) {
        Log.d("password", password);
        UserPojo userPojo = new UserPojo(name, mail, password, gender, age, nonce);
        SignupService signupService = RetrofitClient.getSignupService();
        signupService.register(userPojo).enqueue(new Callback<SignupService.Message>() {
            @Override
            public void onResponse(Call<SignupService.Message> call, Response<SignupService.Message> response) {
                Log.i("responseCode", " " + response.code());
                switch (response.code()) {
                    case OnRegisterCallBack.SUCCESS:
                        if (registerCallBack != null) {
                            registerCallBack.onRegisterSuccess(response.body());
                        }
                        break;
                        default:
                            if (registerCallBack != null) {
                                registerCallBack.onRegisterFail(response.code());
                            }
                }
            }

            @Override
            public void onFailure(Call<SignupService.Message> call, Throwable t) {
                t.printStackTrace();
                if (registerCallBack != null) {
                    registerCallBack.onRegisterFail(OnRegisterCallBack.REQUEST_FAIL);
                }
            }
        });
    }

    public void login() {
        if (this.mail == null || this.password == null) {
            return;
        }
        Log.d("login: ", "email: " + this.mail + "; pass: "+ this.password);
        SigninService signinService = RetrofitClient.getSigninService();
        signinService.login(new SigninService.SignBody(this.mail, this.password))
                .enqueue(new Callback<SigninService.SigninResponse>() {
            @Override
            public void onResponse(Call<SigninService.SigninResponse> call, Response<SigninService.SigninResponse> response) {
                Log.d("Sign In", "response code: " + response.code());
                switch (response.code()) {
                    case OnLoginCallBack.SUCCESS:
                        if (onLoginCallBack != null) {
                            onLoginCallBack.onLoginSuccess(response.body());
                        }
                        break;
                    default:
                        if (onLoginCallBack != null) {
                            onLoginCallBack.onLoginFail(response.code());
                        }
                }
            }

            @Override
            public void onFailure(Call<SigninService.SigninResponse> call, Throwable t) {
                t.printStackTrace();
                if (onLoginCallBack != null) {
                    onLoginCallBack.onLoginFail(OnLoginCallBack.REQUEST_FAIL);
                }
            }
        });
    }

    public void logout() {
        // clear shareReferences
        SharedPreferences.Editor editor = activity.getPreferences(Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        // perform logout
        Navigation.findNavController(activity, R.id.profileNavHostFragment).popBackStack();
        UserAuth.getInstance().setState(UserAuth.UN_AUTHENTICATED, -100);
    }

    public interface OnRegisterCallBack {
        int NONCE_NULL = -1;
        int SUCCESS = 200;
        int EMAIL_INVALID = 400;
        int REGISTED_EMAIL = 409;
        int SERVER_ERROR = 500;
        int REQUEST_FAIL = -100;

        void onRegisterSuccess(SignupService.Message message);

        void onRegisterFail(int error);
    }

    public static interface OnLoginCallBack {
        int SUCCESS = 200;
        int REQUEST_FAIL = -100;

        void onLoginSuccess(SigninService.SigninResponse message);

        void onLoginFail(int error);
    }

    public Bundle toBundle() {
        Bundle user = new Bundle();
        user.putInt("id", id);
        user.putString("authen_token", authen_token);
        user.putString("phone", phone);
        user.putString("mail", mail);
        user.putString("password", password);
        user.putString("name", name);
        user.putString("decription", decription);
        user.putString("gender", gender);
        user.putInt("age", age);
        user.putString("workplace", workplace);
        user.putString("city", city);

        return user;
    }

    public static User getUserFromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        User user = new User(
                bundle.getInt("id"),
                bundle.getString("mail"),
                bundle.getString("password"),
                bundle.getString("name"),
                bundle.getInt("age"),
                bundle.getString("gender"),
                bundle.getString("phone"),
                bundle.getString("decription"),
                bundle.getString("city"),
                bundle.getString("workplace"));

        user.setAuthen_token(bundle.getString("authen_token"));
        return user;
    }

    /**
     * Description: perform dislike a friend with friendId
     * @param friendId: id of friend is disliked
     */
    public void dislikeFriend(int friendId) {
        Log.d("dislike friend", "id = " + friendId);

        JSONObject data = new JSONObject();
        try {
            // current user_id
            data.put("passer_id", this.getId());

            // passed user_id
            data.put("passed_id", friendId);

            // call socketIO to push data to the server
            UserAuth.getInstance().getSocketIO().push_data(data, "pass");
        } catch (JSONException e) {
            Log.e("JSON exception", e.toString());
        }
    }

    /**
     * Description: perform like a friend with friendId
     * @param friendId: id of friend is liked
     */
    public void likeFriend(int friendId) {
        Log.d("like friend", "id = " + friendId);

        JSONObject data = new JSONObject();
        try {
            // current user_id
            data.put("liker_id", this.getId());

            // liked user_id
            data.put("liked_id", friendId);

            // call socketIO to push data to the server
            UserAuth.getInstance().getSocketIO().push_data(data, "like");
        } catch (JSONException e) {
            Log.e("JSON exception", e.toString());
        }
    }

    /**
     *
     * @param n: the number of image
     * @return: url of n th image
     */
    public static String getImageUrl(int id, int n) {
        return RetrofitClient.BASE_URL + "/upload/"+ id +"_image" + n + ".jpg";
    }

    public static String getDefaultAvatarUrl(String gender) {
        return RetrofitClient.BASE_URL + "/upload/" + gender + ".jpg";
    }

    public void updateUserSettings(final String swipe_gender, final int min_age, final int max_age, final int max_distance) {
        UpdateUserService.UpdateSettingBody body = new UpdateUserService.UpdateSettingBody(swipe_gender, min_age, max_age, max_distance);
        RetrofitClient.getUpdateUserService().updateUserSettings(getHeaderAuthenToken(), body)
                .enqueue(new Callback<MessageError>() {
                    @Override
                    public void onResponse(Call<MessageError> call, Response<MessageError> response) {
                        if (response.code() == 200) {
                            Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
                            User.this.swipe_gender = swipe_gender;
                            User.this.min_age = min_age;
                            User.this.max_age = max_age;
                            User.this.max_distance = max_distance;
                            updateSettingToLocal();
                        } else if (response.code() == 400){
                            Toast.makeText(activity, "Information is invalid", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageError> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(activity, "Error Connect", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUserInfo (final String name, final String gender, final int age, final String phone, final String description, final String city, final String workplace) {
        UpdateUserService.UpdateInfoBody body = new UpdateUserService.UpdateInfoBody(name, gender, age, phone, description, city, workplace);
        RetrofitClient.getUpdateUserService().updateUserInfo(getHeaderAuthenToken(), body)
                .enqueue(new Callback<MessageError>() {
                    @Override
                    public void onResponse(Call<MessageError> call, Response<MessageError> response) {
                        if (response.code() == 200) {
                            Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
                            User.this.name = name;
                            User.this.gender = gender;
                            User.this.age = age;
                            User.this.phone = phone;
                            User.this.decription = description;
                            User.this.city = city;
                            User.this.workplace = workplace;
                            updateInfoToLocal();
                        } else if (response.code() == 400) {
                            Toast.makeText(activity, "Information is invalid", Toast.LENGTH_SHORT).show();
                            Log.d("update", "data is invalid");
                        } else {
                            Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageError> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(activity, "Error Connect", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity context) {
        this.activity = context;
    }

    public String getSwipe_gender() {
        return swipe_gender;
    }

    public void setSwipe_gender(String swipe_gender) {
        this.swipe_gender = swipe_gender;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        this.password = new String(Hex.encodeHex(DigestUtils.md5(password)));
    }

    public void setHashedPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthen_token() {
        return authen_token;
    }

    public String getHeaderAuthenToken() {
        return "Barer " + this.authen_token;
    }

    public void setAuthen_token(String authen_token) {
        this.authen_token = authen_token;
    }

    public int getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(int longtitude) {
        this.longtitude = longtitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(int max_distance) {
        this.max_distance = max_distance;
    }

    public int getMin_age() {
        return min_age;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public int getMax_age() {
        return max_age;
    }

    public void setMax_age(int max_age) {
        this.max_age = max_age;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean isIs_banned() {
        return is_banned;
    }

    public void setIs_banned(boolean is_banned) {
        this.is_banned = is_banned;
    }

    public String getBan_reason() {
        return ban_reason;
    }

    public void setBan_reason(String ban_reason) {
        this.ban_reason = ban_reason;
    }

    public String getExprired_ban() {
        return exprired_ban;
    }

    public void setExprired_ban(String exprired_ban) {
        this.exprired_ban = exprired_ban;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
