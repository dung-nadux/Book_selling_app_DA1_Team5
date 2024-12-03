package fpoly.dungnm.duan1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import fpoly.dungnm.duan1.MainActivity;
import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView tvRegister;
    private TextInputEditText edtUserNameLogin, edtPasswordLogin;
    private HttpRequest request;
    private String checkRole = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        anhXa();
        request = new HttpRequest();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserNameLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();
                request.callAPI().login(userName, password).enqueue(callbackLogin);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        }


    private void anhXa() {
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        edtUserNameLogin = findViewById(R.id.edtUserNameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
    }

    private void saveUser(){

    }

    Callback<ResponeData<User>> callbackLogin = new Callback<ResponeData<User>>() {
        @Override
        public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        User user = response.body().getData();
                        if (user.getStatus().equals("Banned")) {
                            Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getSharedPreferences("CHECK_LOGIN", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", user.getUsername());
                            editor.putString("password",user.getPassword());
                            editor.putFloat("balance", (float) user.getBalance());
                            editor.putString("USER_ID", user.getId());
                            editor.apply();
                            if (user.getUsername().equals("admin") ) {
                                Intent intent = new Intent(LoginActivity.this, MainAdminActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(LoginActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        @Override
        public void onFailure(Call<ResponeData<User>> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            Log.e("er", "Lỗi kết nối login: "+ t.getMessage());
        }
    };
}