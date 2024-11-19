package fpoly.dungnm.book_selling_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DBBookInfo", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng PRODUCTS
        String createTable = "CREATE TABLE PRODUCTS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image BLOB, " + // Đổi từ TEXT sang BLOB
                "title TEXT, " +
                "author TEXT, " +
                "price INTEGER, " +
                "description TEXT, " +
                "category TEXT," +
                "quantity INTEGER DEFAULT 1" +
                ");";
        db.execSQL(createTable);

        String createTableCart = "CREATE TABLE CART(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image BLOB, " +
                "title TEXT, " +
                "author TEXT, " +
                "price INTEGER, " +
                "description TEXT, " +
                "category TEXT," +
                "quantity INTEGER DEFAULT 1" +
                ");";
        db.execSQL(createTableCart);

        String createTableAdress = "CREATE TABLE ADDRESS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "receive TEXT, "+
                "adress TEXT " +
                ");";
        db.execSQL(createTableAdress);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng nếu đã tồn tại và tạo lại
        String dropTable = "DROP TABLE IF EXISTS PRODUCTS";
        db.execSQL(dropTable);

        String dropTable1 = "DROP TABLE IF EXISTS CART";
        db.execSQL(dropTable1);

        onCreate(db);
    }
}
