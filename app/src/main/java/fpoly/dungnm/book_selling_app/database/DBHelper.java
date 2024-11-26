package fpoly.dungnm.book_selling_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DBBookInfo", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng PRODUCTS
        String createTableProduct = "CREATE TABLE PRODUCTS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image BLOB, " + // Đổi từ TEXT sang BLOB
                "title TEXT, " +
                "author TEXT, " +
                "price REAL, " +
                "description TEXT, " +
                "category INTEGER," +
                "quantity INTEGER DEFAULT 1," +
                "status INTEGER DEFAULT 1," +
                "FOREIGN KEY (category) REFERENCES CATEGORY(id));";
        db.execSQL(createTableProduct);

        String createTableCategory = "CREATE TABLE CATEGORY(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category TEXT);";
        db.execSQL(createTableCategory);

        String createTableCart = "CREATE TABLE CART(" +
                "UserID INTEGER, " +
                "BookID INTEGER, " +
                "quantity INTEGER, " +
                "Amount REAL, " +
                "PRIMARY KEY (UserID, BookID), " +
                "FOREIGN KEY (UserID) REFERENCES USER(id), " + // Khóa ngoại tới bảng USER
                "FOREIGN KEY (BookID) REFERENCES PRODUCTS(id));"; // Khóa ngoại tới bảng PRODUCTS
        db.execSQL(createTableCart);


        String createTableUser = "CREATE TABLE USER(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT," +
                "fullname TEXT, "+
                "phone INTEGER, "+
                "address TEXT, " +
                "status INTEGER DEFAULT 1);";
        db.execSQL(createTableUser);

        String insertCategory = "INSERT INTO CATEGORY (category) VALUES " +
                "('Truyện tranh'), " +
                "('Học thuật'), " +
                "('Đời sống'), " +
                "('Tiểu thuyết'), " +
                "('Nấu ăn');";
        db.execSQL(insertCategory);

        // Thêm dữ liệu vào bảng USER
        String insertUser = "INSERT INTO USER (email, password, fullname, phone, address) VALUES " +
                "('admin@gmail.com', 'admin@', 'Admin', '0123456789','Book Selling')," +
                "('loi001@gmail.com', 'loi001', 'loi', '0123456789','Book Selling')"
                ;
        db.execSQL(insertUser);


        String createTablePay = "CREATE TABLE PAY(" +
                "id INTEGER PRIMARY KEY , " +
                "UserID INTEGER, " +
                "pay REAL, "+
                "FOREIGN KEY (UserID) REFERENCES USER(id) " +
                ");";
        db.execSQL(createTablePay);

        String createTableAdress = "CREATE TABLE ADDRESS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "UserID INTEGER, " +
                "fullname TEXT, "+
                "phone INTEGER, "+
                "address TEXT ," +
                "FOREIGN KEY (UserID) REFERENCES USER(id) " +
                ");";
        db.execSQL(createTableAdress);

        String createOrdersTable = "CREATE TABLE ORDERS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "address TEXT, " +
                "total_amount REAL, " +
                "payment_method TEXT, " +
                "created_at INTEGER," +
                "status TEXT, "+
                "date TEXT," +
                "FOREIGN KEY (UserID) REFERENCES USER(id) " +
                ")";
        db.execSQL(createOrdersTable);

        String createOrderDetailsTable = "CREATE TABLE OrderDetails (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "product_name TEXT, " +
//                "product_id INTEGER, " +
                "quantity INTEGER, " +
//                "price REAL, " +
                "FOREIGN KEY(order_id) REFERENCES ORDERS(id)" +
//                "FOREIGN KEY(product_id) REFERENCES PRODUCTS(id)" +
                ")";
        db.execSQL(createOrderDetailsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng nếu đã tồn tại và tạo lại
        String dropTable = "DROP TABLE IF EXISTS PRODUCTS";
        db.execSQL(dropTable);

        String dropTable0 = "DROP TABLE IF EXISTS CATEGORY";
        db.execSQL(dropTable0);

        String dropTable1 = "DROP TABLE IF EXISTS CART";
        db.execSQL(dropTable1);

        String dropTable2 = "DROP TABLE IF EXISTS USER";
        db.execSQL(dropTable2);

        String dropTable3 = "DROP TABLE IF EXISTS PAY";
        db.execSQL(dropTable3);

        String dropTable6 = "DROP TABLE IF EXISTS ADDRESS";
        db.execSQL(dropTable6);

        String dropTable4 = "DROP TABLE IF EXISTS ORDERS";
        db.execSQL(dropTable4);

        String dropTable5 = "DROP TABLE IF EXISTS OrderDetails";
        db.execSQL(dropTable5);



        onCreate(db);
    }
}
