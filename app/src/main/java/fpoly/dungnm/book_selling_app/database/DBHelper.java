package fpoly.dungnm.book_selling_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DBBookInfo", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng book với các cột id, image, title, author, price, description, category
        String createTable = "CREATE TABLE PRODUCTS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image TEXT, " +
                "title TEXT, " +
                "author TEXT, " +
                "price INTEGER, " +
                "description TEXT, " +
                "category TEXT);";
        db.execSQL(createTable);

        // Chèn các giá trị mẫu vào bảng book
        String insertSampleData = "INSERT INTO PRODUCTS(image, title, author, price, description, category) VALUES " +
                "( 'image1.png', 'Learning Java', 'Author A', 100, 'Java basics', 'Education'), " +
                "( 'image2.png', 'Learning C++', 'Author B', 150, 'C++ basics', 'Education'), " +
                "('image3.png', 'Learning Python', 'Author C', 120, 'Python basics', 'Education');";
        db.execSQL(insertSampleData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng nếu đã tồn tại và tạo lại
        String dropTable = "DROP TABLE IF EXISTS PRODUCTS";
        db.execSQL(dropTable);
        onCreate(db);
    }
}
