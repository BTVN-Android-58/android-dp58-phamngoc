package com.rsui.rs_network.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_app_shared_pref";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_USER_NAME = "key_user_name";
    private static final String KEY_USER_EMAIL = "key_user_email";
    private static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    private static SharedPrefManager instance;
    private final Context context;
    private SharedPrefManager(Context context) {
        this.context = context.getApplicationContext();
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }
    // Lưu thông tin người dùng và token khi đăng nhập thành công
    public void saveUserData(String token, String refreshToken, int userId, String userName, String userEmail) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply(); // Ghi dữ liệu bất đồng bộ xuống disk
    }
    // Cập nhật cặp token mới khi thực hiện xoay vòng token (Refresh Token)
    public void updateTokens(String newToken, String newRefreshToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, newToken);
        editor.putString(KEY_REFRESH_TOKEN, newRefreshToken);
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false) && getToken() != null;
    }
    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
    public String getRefreshToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }
    // Xóa sạch dữ liệu lưu trữ khi Logout hoặc Token hết hạn
    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
