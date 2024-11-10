package fpoly.dungnm.book_selling_app.login.forgotpass;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.dungnm.book_selling_app.R;

public class CodeActivity extends AppCompatActivity {
private TextView countdownTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code);
        countdownTextView = findViewById(R.id.timeDown); // Khởi tạo TextView

        // Bắt đầu đếm ngược 60 giây
        startCountdown(60000);
    }

    private void startCountdown(long millis) {
        new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật TextView với thời gian còn lại
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                countdownTextView.setText("Gửi lại trong " + secondsRemaining + " giây");
            }

            @Override
            public void onFinish() {
                // Khi đếm ngược kết thúc
                countdownTextView.setText("Gửi lại");
            }
        }.start();
    }
}