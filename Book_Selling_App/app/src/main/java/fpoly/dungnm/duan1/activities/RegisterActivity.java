package fpoly.dungnm.duan1.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private ImageView btnBackRegister;
    private TextInputEditText edtUserName, edtPassword, edtConfirmPassword, edtFullName, edtAddress, edtPhone;
    private HttpRequest request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();
        request = new HttpRequest();


        btnRegister.setOnClickListener(v -> {
            register();
        });

        btnBackRegister.setOnClickListener(v -> {
            finish();
        });
    }

    private void register() {
        String username = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullname(fullName);
        newUser.setPhone(phone);
        newUser.setAddress(address);

        request.callAPI().register(newUser).enqueue(registerCallback);
    }

    private void anhXa() {
        btnRegister = findViewById(R.id.btnRegister);
        btnBackRegister = findViewById(R.id.btnBackRegister);
        edtUserName = findViewById(R.id.edtUserNameRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPasswordRegister);
        edtFullName = findViewById(R.id.edtFullNameRegister);
        edtAddress = findViewById(R.id.edtAddressRegister);
        edtPhone = findViewById(R.id.edtPhoneRegister);

    }

    Callback<ResponeData<User>> registerCallback = new Callback<ResponeData<User>>() {
        @Override
        public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        User user = response.body().getData();
                        if (user != null) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Có lỗi xảy ra: " + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Phản hồi không hợp lệ từ server", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<User>> call, Throwable t) {
            Toast.makeText(RegisterActivity.this, "Lỗi kết nối đến server register: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("er", t.getMessage());
        }
    };
}