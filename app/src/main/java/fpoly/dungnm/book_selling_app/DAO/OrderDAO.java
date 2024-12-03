package fpoly.dungnm.book_selling_app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelOrder;

public class OrderDAO {
    SQLiteDatabase database;
    DBHelper db;
    public OrderDAO(Context context) {
        db = new DBHelper(context);
        database = db.getWritableDatabase();
    }

    public ModelOrder insert(ModelOrder order) {
        ContentValues values = new ContentValues();
        values.put("UserID", order.getUserId());
        values.put("discountVoucherID", order.getDiscountVoucherID());
        values.put("shippingVoucherID", order.getShippingVoucherID());
        values.put("status", order.getStatus());
        values.put("totalPrice", order.getTotalPrice());
        long result = database.insert("Orders", null, values);
        if (result == -1) {
            return null;
        } else {
            order.setId((int) result);
            return order;
        }
    }

    public ArrayList<ModelOrder> getOrderByStatus (String status) {
        ArrayList<ModelOrder> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Orders WHERE status = ?", new String[]{status});
        if (cursor.moveToFirst()) {
            do {
                list.add(new ModelOrder(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // userId
                        cursor.getInt(2), // discountVoucherID
                        cursor.getInt(3), // shippingVoucherID
                        cursor.getString(4), // status
                        cursor.getDouble(5), // totalPrice
                        cursor.getString(6), // date
                        cursor.getInt(7)     // addressId
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deleteOrder(int id) {
        int deletes = database.delete("ORDERDETAIL", "OrderID = ?", new String[]{String.valueOf(id)});
        long result = database.delete("Orders", "id = ?", new String[]{String.valueOf(id)});
        return result != -1 && deletes > 0;
    }

    public boolean updateOrderStatus(ModelOrder order) {
        ContentValues values = new ContentValues();
        values.put("status", order.getStatus());
        long result = database.update("Orders", values, "id = ?", new String[]{String.valueOf(order.getId())});
        return result != -1;
    }

    public boolean updateOrder(ModelOrder order) {
        ContentValues values = new ContentValues();
        values.put("discountVoucherID", order.getDiscountVoucherID());
        values.put("shippingVoucherID", order.getShippingVoucherID());
        values.put("totalPrice", order.getTotalPrice());
        long result = database.update("Orders", values, "id = ?", new String[]{String.valueOf(order.getId())});
        return result != -1;
    }
}
