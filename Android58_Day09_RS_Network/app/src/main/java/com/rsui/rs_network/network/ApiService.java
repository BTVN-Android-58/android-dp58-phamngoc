package com.rsui.rs_network.network;

import com.rsui.rs_network.model.User;
import com.rsui.rs_network.network.dto.LoginRequest;
import com.rsui.rs_network.network.dto.LoginResponse;
import com.rsui.rs_network.network.dto.ProductListResponse;
import com.rsui.rs_network.network.dto.RefreshRequest;
import com.rsui.rs_network.network.dto.TokenResponse;
import com.rsui.rs_network.network.dto.UserListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users")
    Call<UserListResponse> getUsers();

    @GET("products")
    Call<ProductListResponse> getProducts();

    @GET("users/{id}")
    Call<User> getUser(@Path("id") int userId);

    @POST("refresh")
    Call<TokenResponse> refreshToken(@Body RefreshRequest refreshToken);
}
