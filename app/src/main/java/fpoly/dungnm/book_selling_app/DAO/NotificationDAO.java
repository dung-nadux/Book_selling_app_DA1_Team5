package fpoly.dungnm.book_selling_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private SQLiteDatabase db;

    public NotificationDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Create
    public boolean addNotification(String email, String title, String content) {
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("title", title);
        values.put("content", content);

        long result = db.insert("NOTIFICATION", null, values);
        return result != -1;
    }

    // Read
    public List<String> getNotificationsByEmail(String email) {
        List<String> notifications = new ArrayList<>();
        Cursor cursor = db.query(
                "NOTIFICATION",
                new String[]{"title", "content"},
                "email = ?",
                new String[]{email},
                null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                notifications.add("Title: " + title + "\nContent: " + content);
            }
            cursor.close();
        }
        return notifications;
    }

    // Update
    public boolean updateNotification(String email, String oldTitle, String newTitle, String newContent) {
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        values.put("content", newContent);

        int result = db.update(
                "NOTIFICATION",
                values,
                "email = ? AND title = ?",
                new String[]{email, oldTitle}
        );
        return result > 0;
    }

    // Delete
    public boolean deleteNotification(String email, String title) {
        int result = db.delete(
                "NOTIFICATION",
                "email = ? AND title = ?",
                new String[]{email, title}
        );
        return result > 0;
    }
}
