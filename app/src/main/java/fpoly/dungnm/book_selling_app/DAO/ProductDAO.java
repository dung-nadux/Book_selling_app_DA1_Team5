package fpoly.dungnm.book_selling_app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class ProductDAO {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    // Lấy tất cả sản phẩm
    public ArrayList<ModelProducts> getAllProducts() {
        ArrayList<ModelProducts> productList = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM PRODUCTS", null);
        if (cursor.moveToFirst()) {
            do {
                productList.add(new ModelProducts(
                        cursor.getInt(0),     // id
                        cursor.getString(1),     // image
                        cursor.getString(2),     // title
                        cursor.getString(3),     // author
                        cursor.getInt(4),        // price
                        cursor.getString(5),     // description
                        cursor.getString(6)      // category
                ));
            } while (cursor.moveToNext());
        }
//        cursor.close();
        return productList;
    }

    // Thêm sản phẩm mới
    public boolean insertProduct(ModelProducts product) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", product.getId());
        values.put("image", product.getImage());
        values.put("title", product.getTitle());
        values.put("author", product.getAuthor());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        long result = database.insert("book", null, values);
        return result != -1;
    }

    // Cập nhật thông tin sản phẩm
    public boolean updateProduct(ModelProducts product) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", product.getImage());
        values.put("title", product.getTitle());
        values.put("author", product.getAuthor());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        int result = database.update("book", values, "id = ?", new String[]{product.getId()+""});
        return result > 0;
    }

    // Xóa sản phẩm
    public boolean deleteProduct(String id) {
        database = dbHelper.getWritableDatabase();
        int result = database.delete("book", "id = ?", new String[]{id});
        return result > 0;
    }

    // Cập nhật trạng thái hoặc thuộc tính cụ thể (Ví dụ: cập nhật giá)
    public boolean updateProductPrice(String id, int newPrice) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", newPrice);
        int result = database.update("book", values, "id = ?", new String[]{id});
        return result > 0;
    }
}
