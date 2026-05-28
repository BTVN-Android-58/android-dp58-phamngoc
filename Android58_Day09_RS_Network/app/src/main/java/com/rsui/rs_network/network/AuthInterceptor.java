package com.rsui.rs_network.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final TokenRepository tokenRepo;

    public AuthInterceptor(TokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 1. Tự động đính kèm Access Token vào header nếu có
        String token = tokenRepo.getAccessToken();
        if (token != null) {
            request = request.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        // 2. Thực hiện request và nhận response
        Response response = chain.proceed(request);

        // 3. Nếu nhận mã 401 hoặc 403 (Token hết hạn hoặc không hợp lệ)
        if (response.code() == 401 || response.code() == 403) {
            // Tiến hành làm mới token đồng bộ
            if (tokenRepo.refreshToken()) {
                // Đóng response cũ trước khi retry để tránh rò rỉ tài nguyên
                response.close();

                // Làm mới token thành công -> Lấy token mới vừa được lưu
                String newToken = tokenRepo.getAccessToken();

                // Tạo request mới thay thế Authorization Header bằng Access Token mới
                Request retry = request.newBuilder()
                        .header("Authorization", "Bearer " + newToken)
                        .build();

                // Gửi lại request cũ (Retry)
                return chain.proceed(retry);
            } else {
                // Refresh thất bại (Refresh Token cũng hết hạn) -> Buộc đăng xuất
                tokenRepo.forceLogout();
            }
        }

        return response;
    }
}
