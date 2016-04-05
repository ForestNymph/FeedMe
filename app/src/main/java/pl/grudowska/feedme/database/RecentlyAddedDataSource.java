package pl.grudowska.feedme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecentlyAddedDataSource {

    // Database fields
    private SQLiteDatabase database;
    private RecentlyAddedDB_SQL dbHelper;
    private String[] allColumns = {RecentlyAddedDB_SQL.COLUMN_ID,
            RecentlyAddedDB_SQL.COLUMN_PRODUCT, RecentlyAddedDB_SQL.COLUMN_AMOUNT};

    public RecentlyAddedDataSource(Context context) {
        dbHelper = new RecentlyAddedDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public RecentlyAdded createProduct(String product, int amount) {
        ContentValues values = new ContentValues();
        values.put(RecentlyAddedDB_SQL.COLUMN_PRODUCT, product);
        values.put(RecentlyAddedDB_SQL.COLUMN_AMOUNT, amount);
        long insertId = database.insert(RecentlyAddedDB_SQL.TABLE_RECENTLY, null,
                values);
        Cursor cursor = database.query(RecentlyAddedDB_SQL.TABLE_RECENTLY,
                allColumns, RecentlyAddedDB_SQL.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        RecentlyAdded newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }

    public void deleteProduct(RecentlyAdded product) {
        long id = product.getId();
        System.out.println("Product deleted with id: " + id);
        database.delete(RecentlyAddedDB_SQL.TABLE_RECENTLY, RecentlyAddedDB_SQL.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll() {
        database.execSQL("delete from " + RecentlyAddedDB_SQL.TABLE_RECENTLY);
    }

    public List<RecentlyAdded> getAllAddedProducts() {
        List<RecentlyAdded> products = new ArrayList<RecentlyAdded>();

        Cursor cursor = database.query(RecentlyAddedDB_SQL.TABLE_RECENTLY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecentlyAdded product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return products;
    }

    private RecentlyAdded cursorToProduct(Cursor cursor) {
        RecentlyAdded product = new RecentlyAdded();
        product.setId(cursor.getLong(0));
        product.setProduct(cursor.getString(1));
        product.setAmount(cursor.getInt(2));
        return product;
    }
}
