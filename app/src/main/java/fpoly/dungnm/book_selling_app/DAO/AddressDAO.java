package fpoly.dungnm.book_selling_app.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelAddres;

public class AddressDAO {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public AddressDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Lấy tất cả địa chỉ
    public ArrayList<ModelAddres> getAllAddresses(int userId) {
        ArrayList<ModelAddres> addressList = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ADDRESS WHERE UserID = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                addressList.add(new ModelAddres(
                        cursor.getInt(0),       // id
                        cursor.getInt(1),    // fullname
                        cursor.getString(2),    // fullname
                        cursor.getString(3),       // phone
                        cursor.getString(4)     // address
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return addressList;
    }

    // Thêm địa chỉ mới
    public boolean insertAddress(ModelAddres address) {
        ContentValues values = new ContentValues();
        values.put("UserID", address.getUserId());
        values.put("fullname", address.getFullName());
        values.put("phone", address.getPhone());
        values.put("address", address.getAddress());
        long result = database.insert("ADDRESS", null, values);
        return result > 0;
    }

    // Cập nhật địa chỉ
    public boolean updateAddress(ModelAddres address) {
        ContentValues values = new ContentValues();
        values.put("fullname", address.getFullName());
        values.put("phone", address.getPhone());
        values.put("address", address.getAddress());
        int result = database.update("ADDRESS", values, "id = ?", new String[]{String.valueOf(address.getId())});
        return result > 0;
    }

    // Xóa địa chỉ
    public boolean deleteAddress(int id) {
        int result = database.delete("ADDRESS", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Lấy địa chỉ theo ID
    public ModelAddres getAddressById(int id) {
        ModelAddres address = null;
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ADDRESS WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            address = new ModelAddres(
                    cursor.getInt(0),       // userId
                    cursor.getString(1),    // fullname
                    cursor.getString(2),       // phone
                    cursor.getString(3)     // address
            );
        }
        if (cursor != null) {
            cursor.close();
        }
        return address;
    }
}
