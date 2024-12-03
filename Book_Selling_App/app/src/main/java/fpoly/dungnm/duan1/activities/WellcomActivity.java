package fpoly.dungnm.duan1.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.dungnm.duan1.MainActivity;
import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WellcomActivity extends AppCompatActivity {

    private String checkRole = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wellcom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String checkUser;
        String checkPass;
        SharedPreferences preferences = getSharedPreferences("CHECK_LOGIN" , Context.MODE_PRIVATE);
        checkUser = preferences.getString("username","1");
        checkPass = preferences.getString("password","");
        if ("0".equals(checkUser)) {
            // Thông báo phiên đăng nhập hết hạn
            Toast.makeText(getApplicationContext(), "Phiên đăng nhập hết hạn hãy đăng nhập lại", Toast.LENGTH_LONG).show();
            handler(LoginActivity.class);
        } else if ("1".equals(checkUser)) {
            Toast.makeText(getApplicationContext(), "Wellcome!", Toast.LENGTH_LONG).show();
            handler(LoginActivity.class);
        } else {
            // Gọi phương thức checkLogin với email và password từ SharedPreferences
            HttpRequest request = new HttpRequest();
            request.callAPI().login(checkUser, checkPass).enqueue(callbackLogin);
        }
    }

    Callback<ResponeData<User>> callbackLogin = new Callback<ResponeData<User>>() {
        @Override
        public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        User user = response.body().getData();
                        if (user.getStatus().equals("Banned")) {
                            Toast.makeText(WellcomActivity.this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WellcomActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = getSharedPreferences("CHECK_LOGIN", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", user.getUsername());
                            editor.putString("password",user.getPassword());
                            editor.putFloat("balance", (float) user.getBalance());
                            editor.putString("USER_ID", user.getId());
                            editor.apply();
                            if (user.getUsername().equals("admin") ) {
                                handler(MainAdminActivity.class);
                            } else {
                                handler(MainActivity.class);
                            }
                        }
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(WellcomActivity.this, "Phiên đăng nhập hết hạn hãy đăng nhập lại", Toast.LENGTH_SHORT).show();
                        handler(LoginActivity.class);
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(WellcomActivity.this, "Phiên đăng nhập hết hạn hãy đăng nhập lại", Toast.LENGTH_SHORT).show();
                        handler(LoginActivity.class);
                    }
                }
            }
        }
        @Override
        public void onFailure(Call<ResponeData<User>> call, Throwable t) {
            Toast.makeText(WellcomActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            handler(LoginActivity.class);
            Log.e("er", "Lỗi kết nối login: "+ t.getMessage());
        }
    };

    private void handler(Class<? extends Activity> activityClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WellcomActivity.this, activityClass);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }

}