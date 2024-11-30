package fpoly.dungnm.book_selling_app.models;

import android.content.Context;

import java.util.List;

import fpoly.dungnm.book_selling_app.database.NotificationDAO;

public class NotificationService {
    private NotificationDAO notificationDAO;

    public NotificationService(Context context) {
        this.notificationDAO = new NotificationDAO(context);
    }

    public boolean createNotification(String email, String title, String content) {
        return notificationDAO.addNotification(email, title, content);
    }

    public List<String> getNotifications(String email) {
        return notificationDAO.getNotificationsByEmail(email);
    }

    public boolean updateNotification(String email, String oldTitle, String newTitle, String newContent) {
        return notificationDAO.updateNotification(email, oldTitle, newTitle, newContent);
    }

    public boolean deleteNotification(String email, String title) {
        return notificationDAO.deleteNotification(email, title);
    }
}
