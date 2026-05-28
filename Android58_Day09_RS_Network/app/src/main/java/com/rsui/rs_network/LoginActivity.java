package com.rsui.rs_network;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rsui.rs_network.auth.SharedPrefManager;
import com.rsui.rs_network.network.ApiCallback;
import com.rsui.rs_network.network.ApiRepository;
import com.rsui.rs_network.network.dto.LoginRequest;
import com.rsui.rs_network.network.dto.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    private ApiRepository apiRepository;
    private SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        if (sharedPrefManager.isLoggedIn()) {
            // Chuyển hướng tới Activity chính (Ví dụ ProductActivity)
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Hủy LoginActivity tránh bấm nút back quay lại được
            return;
        }
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        apiRepository = new ApiRepository();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginRequest request = new LoginRequest(email, password);
        apiRepository.login(request, new ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                // lưu token nhận được từ response
                // Lưu thông tin
                sharedPrefManager.saveUserData(
                        response.getToken(),
                        response.getRefreshToken(),
                        response.getUserId(),
                        response.getUsername(),
                        response.getEmail()
                );
                // Chuyển màn hình
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}