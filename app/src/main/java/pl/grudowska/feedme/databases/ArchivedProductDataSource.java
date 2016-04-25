package pl.grudowska.feedme.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArchivedProductDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ArchivedProductDB_SQL dbHelper;
    private String[] allColumns = {ArchivedProductDB_SQL.COLUMN_ID,
            ArchivedProductDB_SQL.COLUMN_DATE, ArchivedProductDB_SQL.COLUMN_CONTENT_MAIL,
            ArchivedProductDB_SQL.COLUMN_CONTENT_NAME,
            ArchivedProductDB_SQL.COLUMN_CONTENT_AMOUNT, ArchivedProductDB_SQL.COLUMN_KCAL};

    public ArchivedProductDataSource(Context context) {
        dbHelper = new ArchivedProductDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArchivedProduct createArchivedItem(String date, String contentMail, String contentName, String contentAmount, int kcal) {
        ContentValues values = new ContentValues();
        values.put(ArchivedProductDB_SQL.COLUMN_DATE, date);
        values.put(ArchivedProductDB_SQL.COLUMN_CONTENT_MAIL, contentMail);
        values.put(ArchivedProductDB_SQL.COLUMN_CONTENT_NAME, contentName);
        values.put(ArchivedProductDB_SQL.COLUMN_CONTENT_AMOUNT, contentAmount);
        values.put(ArchivedProductDB_SQL.COLUMN_KCAL, kcal);
        long insertId = database.insert(ArchivedProductDB_SQL.TABLE_ARCHIVED, null,
                values);
        Cursor cursor = database.query(ArchivedProductDB_SQL.TABLE_ARCHIVED,
                allColumns, ArchivedProductDB_SQL.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ArchivedProduct newProduct = cursorToArchived(cursor);
        cursor.close();
        return newProduct;
    }

    public void deleteArchivedItem(ArchivedProduct archived) {
        long id = archived.id;
        System.out.println("Product deleted with id: " + id);
        database.delete(ArchivedProductDB_SQL.TABLE_ARCHIVED, ArchivedProductDB_SQL.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllArchivedItems() {
        database.execSQL("delete from " + ArchivedProductDB_SQL.TABLE_ARCHIVED);
    }

    public List<ArchivedProduct> getAllArchivedLists() {
        List<ArchivedProduct> archivedLists = new ArrayList<>();

        Cursor cursor = database.query(ArchivedProductDB_SQL.TABLE_ARCHIVED,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArchivedProduct archive = cursorToArchived(cursor);
            archivedLists.add(archive);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return archivedLists;
    }

    private ArchivedProduct cursorToArchived(Cursor cursor) {
        ArchivedProduct archive = new ArchivedProduct();
        archive.id = cursor.getLong(0);
        archive.date = cursor.getString(1);
        archive.contentMail = cursor.getString(2);
        archive.contentName = cursor.getString(3);
        archive.contentAmount = cursor.getString(4);
        archive.kcal = cursor.getInt(5);
        return archive;
    }
}
