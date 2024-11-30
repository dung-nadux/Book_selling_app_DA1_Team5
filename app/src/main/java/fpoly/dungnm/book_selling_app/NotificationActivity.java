package fpoly.dungnm.book_selling_app;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpoly.dungnm.book_selling_app.models.NotificationService;

public class NotificationActivity extends AppCompatActivity {
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Khởi tạo NotificationService
        notificationService = new NotificationService(this);

        // Ví dụ thêm thông báo mới
        notificationService.createNotification(
                "admin@gmail.com",
                "Welcome",
                "Welcome to the Book Selling App!"
        );

        // Ví dụ đọc thông báo
        List<String> notifications = notificationService.getNotifications("admin@gmail.com");
        for (String notification : notifications) {
            System.out.println(notification); // Hoặc hiển thị trên UI
        }

        // Ví dụ cập nhật thông báo
        notificationService.updateNotification(
                "admin@gmail.com",
                "Welcome",
                "Updated Welcome",
                "Welcome to the updated version of the app!"
        );

        // Ví dụ xóa thông báo
        notificationService.deleteNotification("admin@gmail.com", "Updated Welcome");
    }
}
