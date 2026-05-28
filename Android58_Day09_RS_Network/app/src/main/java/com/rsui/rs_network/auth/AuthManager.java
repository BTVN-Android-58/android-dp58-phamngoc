package com.rsui.rs_network.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.rsui.rs_network.network.dto.LoginResponse;

public class AuthManager {
    private static final String PREF_NAME = "auth_pref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";

    private SharedPreferences sharedPreferences;
    private static AuthManager instance;

    private AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }

    public void saveUserData(LoginResponse loginResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, loginResponse.getToken());
        editor.putString(KEY_REFRESH_TOKEN, loginResponse.getRefreshToken());
        editor.putInt(KEY_USER_ID, loginResponse.getUserId());
        editor.putString(KEY_USER_EMAIL, loginResponse.getEmail());
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void refreshToken(String newToken, String newRefreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, newToken);
        editor.putString(KEY_REFRESH_TOKEN, newRefreshToken);
        editor.apply();
    }
}
