package com.rsui.rs_network.network.dto;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("access_token")
    private String token; // Đây chính là Access Token JWT
    @SerializedName("refresh_token")
    private String refreshToken;
    // Getters và Setters...
    public boolean isSuccess() { return success; }
    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
}
