package pl.grudowska.feedme.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllSentFoodDataSource {

    // Database fields
    private SQLiteDatabase database;
    private AllSentFoodDB_SQL dbHelper;
    private String[] allColumns = {AllSentFoodDB_SQL.COLUMN_ID,
            AllSentFoodDB_SQL.COLUMN_DATE, AllSentFoodDB_SQL.COLUMN_CONTENT, AllSentFoodDB_SQL.COLUMN_KCAL};

    public AllSentFoodDataSource(Context context) {
        dbHelper = new AllSentFoodDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public AllSentFood createSentItem(String data, String content, int kcal) {
        ContentValues values = new ContentValues();
        values.put(AllSentFoodDB_SQL.COLUMN_DATE, data);
        values.put(AllSentFoodDB_SQL.COLUMN_CONTENT, content);
        values.put(AllSentFoodDB_SQL.COLUMN_KCAL, kcal);
        long insertId = database.insert(AllSentFoodDB_SQL.TABLE_SENT, null,
                values);
        Cursor cursor = database.query(AllSentFoodDB_SQL.TABLE_SENT,
                allColumns, AllSentFoodDB_SQL.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AllSentFood newProduct = cursorToSent(cursor);
        cursor.close();
        return newProduct;
    }

    public void deleteSentItem(AllSentFood sent) {
        long id = sent.getId();
        System.out.println("Product deleted with id: " + id);
        database.delete(AllSentFoodDB_SQL.TABLE_SENT, AllSentFoodDB_SQL.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllItems() {
        database.execSQL("delete from " + AllSentFoodDB_SQL.TABLE_SENT);
    }

    public List<AllSentFood> getAllSentLists() {
        List<AllSentFood> sentLists = new ArrayList<>();

        Cursor cursor = database.query(AllSentFoodDB_SQL.TABLE_SENT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AllSentFood sent = cursorToSent(cursor);
            sentLists.add(sent);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return sentLists;
    }

    private AllSentFood cursorToSent(Cursor cursor) {
        AllSentFood sent = new AllSentFood();
        sent.setId(cursor.getLong(0));
        sent.setDate(cursor.getString(1));
        sent.setContent(cursor.getString(2));
        sent.setKcal(cursor.getInt(3));
        return sent;
    }
}
