package fpoly.dungnm.book_selling_app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fpoly.dungnm.book_selling_app.database.DBHelper;
import fpoly.dungnm.book_selling_app.models.ModelWallet;

public class WalletDAO {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public WalletDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public boolean insertWallet(ModelWallet wallet) {
        ContentValues values = new ContentValues();
        values.put("UserID", wallet.getUserId());
        values.put("balance", wallet.getBalance());
        long result = database.insert("WALLET", null, values);
        return result != -1;
    }
    public boolean updateWallet(ModelWallet wallet) {
        ContentValues values = new ContentValues();
        values.put("balance", wallet.getBalance());
        long result = database.update("WALLET", values, "UserID = ?", new String[]{String.valueOf(wallet.getUserId())});
        return result > 0;
    }

    public ModelWallet getWalletByUserId(int userId) {
        ModelWallet wallet = null;
        String query = "SELECT * FROM WALLET WHERE UserID = ?";
        String[] args = {String.valueOf(userId)};
        Cursor cursor = database.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            wallet = new ModelWallet();
            wallet.setUserId(cursor.getInt(0));
            wallet.setBalance(cursor.getDouble(1));
        }
        return wallet;
    }
}
