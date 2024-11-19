package fpoly.dungnm.book_selling_app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class CartDAO {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public ArrayList<ModelProducts> getAllCart() {
        ArrayList<ModelProducts> productList = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CART", null);
        if (cursor.moveToFirst()) {
            do {
                // Chuyển đổi image từ byte array
                byte[] imageBytes = cursor.getBlob(1);
                productList.add(new ModelProducts(
                        cursor.getInt(0),      // id
                        imageBytes,            // image
                        cursor.getString(2),   // title
                        cursor.getString(3),   // author
                        cursor.getInt(4),      // price
                        cursor.getString(5),   // description
                        cursor.getString(6),   // category
                        cursor.getInt(7)       // quantity
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Thêm sản phẩm mới
//     public boolean insertCart(ModelProducts product) {
//         ContentValues values = new ContentValues();
// //        values.put("id", product.getId()); // Đảm bảo rằng bạn đã có trường id trong database
//         values.put("image", product.getImage());  // Chuyển ảnh vào BLOB
//         values.put("title", product.getTitle());
//         values.put("author", product.getAuthor());
//         values.put("price", product.getPrice());
//         values.put("description", product.getDescription());
//         values.put("category", product.getCategory());
//         long result = database.insert("CART", null, values);
//         if (result == -1) {
//             Log.e("CartDAO", "Lỗi khi thêm sản phẩm vào giỏ hàng");
//             return false;
//         }
//         return true;
//     }
public boolean insertOrUpdateCart(ModelProducts product) {
    // Check if the product already exists in the cart
    ModelProducts existingProduct = getCartById(product.getId());
    if (existingProduct != null) {
        // If it exists, update the quantity
        int newQuantity = existingProduct.getQuantity() + product.getQuantity();
        return updateQuantity(product.getId(), newQuantity);
    } else {
        // If it doesn't exist, insert the new product
        ContentValues values = new ContentValues();
        values.put("image", product.getImage());
        values.put("title", product.getTitle());
        values.put("author", product.getAuthor());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        values.put("quantity", product.getQuantity());

        long result = database.insert("CART", null, values);
        if (result == -1) {
            Log.e("CartDAO", "Lỗi khi thêm sản phẩm vào giỏ hàng");
            return false;
        }
        return true;
    }
}

public boolean updateQuantity(int productId, int quantity) {
    ContentValues values = new ContentValues();
    values.put("quantity", quantity); // Update the quantity

    int result = database.update("CART", values, "id = ?", new String[]{String.valueOf(productId)});
    return result > 0;
}


    // Cập nhật thông tin sản phẩm
    public boolean updateCart(ModelProducts product) {
        ContentValues values = new ContentValues();
        values.put("image", product.getImage());  // Chuyển ảnh vào BLOB
        values.put("title", product.getTitle());
        values.put("author", product.getAuthor());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        int result = database.update("CART", values, "id = ?", new String[]{String.valueOf(product.getId())});
        return result > 0;
    }

    // Xóa sản phẩm
    public boolean deleteCart(int id) {
        int result = database.delete("CART", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Lấy sản phẩm theo ID
    public ModelProducts getCartById(int id) {
        ModelProducts product = null;
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CART WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(1);
            product = new ModelProducts(
                    cursor.getInt(0),      // id
                    imageBytes,            // image
                    cursor.getString(2),   // title
                    cursor.getString(3),   // author
                    cursor.getInt(4),      // price
                    cursor.getString(5),   // description
                    cursor.getString(6)    // category
            );
        }
        if (cursor != null) {
            cursor.close();
        }
        return product;
    }

}
