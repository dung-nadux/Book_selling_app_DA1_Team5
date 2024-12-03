package fpoly.dungnm.book_selling_app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelCart;
import fpoly.dungnm.book_selling_app.models.ModelOrderDetail;

public class OrderDetailDAO {
    SQLiteDatabase database;
    DBHelper db;
    public OrderDetailDAO(Context context) {
        db = new DBHelper(context);
        database = db.getWritableDatabase();
    }

    public void insertFromCart(int userID, int orderID) {
        ArrayList<ModelCart> listCart = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM CART WHERE UserID = ? AND STATUS = 1", new String[]{String.valueOf(userID)});
        Log.e("tttttt", "select: "+cursor.getCount() );
        if (cursor.moveToFirst()) {
            do {
                listCart.add(new ModelCart(
                        cursor.getInt(0),  // userID
                        cursor.getInt(1),  // bookID
                        cursor.getInt(2),  // quantity
                        cursor.getDouble(3),  // amount
                        cursor.getInt(4)));// status
            } while (cursor.moveToNext());
        }
        for (ModelCart cart : listCart) {
            ContentValues values = new ContentValues();
            values.put("orderID", orderID);
            values.put("bookID", cart.getBookID());
            values.put("quantity", cart.getQuantity());
            values.put("unitPrice", cart.getAmount()/cart.getQuantity());
            values.put("Amount", cart.getAmount());
            database.insert("ORDERDETAIL", null, values);
        }
        cursor.close();
    }

    public ArrayList<ModelCart> getCartByStatus(int userID) {
        ArrayList<ModelCart> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM CART WHERE UserID = ? AND STATUS = 1", new String[]{String.valueOf(userID)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new ModelCart(
                        cursor.getInt(0),  // userID
                        cursor.getInt(1),  // bookID
                        cursor.getInt(2),  // quantity
                        cursor.getDouble(3),  // amount
                        cursor.getInt(4)));// status
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<ModelOrderDetail>  getAllOrderDetailByOrderID(int orderID) {
        ArrayList<ModelOrderDetail> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ORDERDETAIL WHERE orderID = ?", new String[]{String.valueOf(orderID)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new ModelOrderDetail(
                        cursor.getInt(0),  // order
                        cursor.getInt(1),  // bookID
                        cursor.getInt(2),  // quantity
                        cursor.getInt(3),  // unitPrice
                        cursor.getInt(4)));// amount
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deleteOrderDetail(int orderId, int bookId) {
        int result = database.delete("ORDERDETAIL", "OrderID = ? AND BookID = ?", new String[]{String.valueOf(orderId), String.valueOf(bookId)});
        return result > 0;
    }
}
