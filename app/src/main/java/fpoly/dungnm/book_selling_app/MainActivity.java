package fpoly.dungnm.book_selling_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.dungnm.book_selling_app.login.LoginActivity;
import fpoly.dungnm.book_selling_app.screens.ScreensActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String checkLogin;
    String checkRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("CHECK_LOGIN" , Context.MODE_PRIVATE);
        checkLogin = preferences.getString("EMAIL","");
        if(!checkLogin.isEmpty()){
            if (checkLogin.equals("admin@gmail.com")) { // So sánh không phân biệt chữ hoa/thường
                checkRole = "1"; // Admin
            } else {
                checkRole = "0"; // Người dùng
            }
            Intent intent = new Intent(this, ScreensActivity.class);
            intent.putExtra("role", checkRole);
            startActivity(intent);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    // Phương thức được gọi khi nhấn nút "Bắt đầu ngay"
    public void batDauNgay(View view) {
        // Tạo Intent để chuyển đến LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}