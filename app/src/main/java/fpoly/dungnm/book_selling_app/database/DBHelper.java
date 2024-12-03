package fpoly.dungnm.book_selling_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DBBookInfo", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng PRODUCTS
        String createTableProduct = "CREATE TABLE PRODUCTS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image BLOB, " + // Đổi từ TEXT sang BLOB
                "title TEXT, " +
                "author TEXT, " +
                "price DOUBLE, " +
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
                "Amount DOUBLE, " +
                "STATUS INTEGER DEFAULT 0," +
                "PRIMARY KEY (UserID, BookID), " +
                "FOREIGN KEY (UserID) REFERENCES USER(id), " + // Khóa ngoại tới bảng USER
                "FOREIGN KEY (BookID) REFERENCES PRODUCTS(id));"; // Khóa ngoại tới bảng PRODUCTS
        db.execSQL(createTableCart);


        String createTableUser = "CREATE TABLE USER(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT," +
                "fullname TEXT, "+
                "phone TEXT, "+
                "address TEXT, " +
                "status INTEGER DEFAULT 1);";
        db.execSQL(createTableUser);

        String createTableAddress = "CREATE TABLE ADDRESS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER," +
                "fullname TEXT," +
                "phone TEXT," +
                "address TEXT)";
        db.execSQL(createTableAddress);

        String createTableWallet = "CREATE TABLE WALLET(" +
                "UserID INTEGER PRIMARY KEY," +
                "balance DOUBLE," +
                "FOREIGN KEY (UserID) REFERENCES USER(id))";
        db.execSQL(createTableWallet);

        String createTableVoucher = "CREATE TABLE VOUCHER(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT," +
                "discount INTEGER," +
                "content TEXT," +
                "startDate DATE," +
                "endDate DATE)";
        db.execSQL(createTableVoucher);

        String createTableOrder = "CREATE TABLE ORDERS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "discountVoucherID INTEGER DEFAULT -1," +
                "shippingVoucherID INTEGER DEFAULT -1," +
                "status TEXT," +
                "totalPrice DOUBLE," +
                "orderDate DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "address INTEGER," +
                "FOREIGN KEY (UserID) REFERENCES USER(id)," +
                "FOREIGN KEY (discountVoucherID) REFERENCES VOUCHER(id)," +
                "FOREIGN KEY (shippingVoucherID) REFERENCES VOUCHER(id)," +
                "FOREIGN KEY (address) REFERENCES ADDRESS(id))";
        db.execSQL(createTableOrder);

        String createTableOrderDetail = "CREATE TABLE ORDERDETAIL(" +
                "OrderID INTEGER, " +
                "BookID INTEGER, " +
                "quantity INTEGER, " +
                "unitPrice DOUBLE," +
                "Amount DOUBLE, " +
                "PRIMARY KEY (OrderID, BookID), " +
                "FOREIGN KEY (OrderID) REFERENCES ORDERS(id), " + // Khóa ngoại tới bảng USER
                "FOREIGN KEY (BookID) REFERENCES PRODUCTS(id));"; // Khóa ngoại tới bảng PRODUCTS
        db.execSQL(createTableOrderDetail);

        String insertCategory = "INSERT INTO CATEGORY (category) VALUES " +
                "('Truyện tranh'), " +
                "('Học thuật'), " +
                "('Đời sống'), " +
                "('Tiểu thuyết'), " +
                "('Nấu ăn');";
        db.execSQL(insertCategory);

        // Thêm dữ liệu vào bảng USER
        String insertUser = "INSERT INTO USER (email, password, fullname, phone, address) VALUES " +
                "('admin@gmail.com', 'admin@', 'Admin', '0123456789','Book Selling');";
        db.execSQL(insertUser);
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

        String dropTable3 = "DROP TABLE IF EXISTS VOUCHER";
        db.execSQL(dropTable3);

        String dropTable4 = "DROP TABLE IF EXISTS ORDERS";
        db.execSQL(dropTable4);

        String dropTable5 = "DROP TABLE IF EXISTS ORDERDETAIL";
        db.execSQL(dropTable5);

        String dropTable6 = "DROP TABLE IF EXISTS WALLET";
        db.execSQL(dropTable6);

        onCreate(db);
    }
}
