package com.rsui.rs_network.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rsui.rs_network.LoginActivity;
import com.rsui.rs_network.auth.AuthManager;
import com.rsui.rs_network.auth.SharedPrefManager;
import com.rsui.rs_network.network.dto.RefreshRequest;
import com.rsui.rs_network.network.dto.TokenResponse;

import retrofit2.Call;
import retrofit2.Response;

public class TokenRepository {

    private final SharedPrefManager sharedPrefs;
    private volatile AuthManager authManager;

    private ApiService apiService;
    private final Context context;

    public TokenRepository(Context context) {
        this.context = context;
        this.sharedPrefs = SharedPrefManager.getInstance(context);
        this.authManager = AuthManager.getInstance(context);
//        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Lấy access token hiện tại
     */
    public String getAccessToken() {
        return sharedPrefs.getToken();
    }

    /**
     * Lấy refresh token hiện tại
     */
    public String getRefreshToken() {
        return sharedPrefs.getRefreshToken();
    }

    /**
     * Thực hiện refresh token đồng bộ (gọi khi 401)
     * @return true nếu refresh thành công và token mới đã được lưu
     */
    public synchronized boolean refreshToken() {
        String refreshToken = getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            forceLogout();
            return false;
        }

        try {
            RefreshRequest request = new RefreshRequest(refreshToken);

            // Sử dụng apiService trực tiếp để gọi refresh token
            Call<TokenResponse> call = apiService.refreshToken(request);

            // execute() là gọi mạng ĐỒNG BỘ (Synchronous), nó sẽ block thread hiện tại
            // (Thread này đang chạy ở background do OkHttp quản lý) cho tới khi nhận về kết quả
            Response<TokenResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                TokenResponse body = response.body();

                // Lưu token mới vào SharedPreferences
                sharedPrefs.updateTokens(body.getAccessToken(), body.getRefreshToken());
                return true;
            } else {
                forceLogout();
            }
        } catch (Exception e) {
            e.printStackTrace();
            forceLogout();
        }

        return false;
    }

    /**
     * Trường hợp Refresh thất bại hoặc không có token -> Bắt buộc Logout người dùng
     */
    public void forceLogout() {
        Log.d("TokenRepository", "Force logout");
        sharedPrefs.clear();
        authManager.logout();

        new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}
