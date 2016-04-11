package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainTypeDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MainTypeDB_SQL dbHelper;
    private String[] allColumns = {MainTypeDB_SQL.COLUMN_ID,
            MainTypeDB_SQL.COLUMN_NAME, MainTypeDB_SQL.COLUMN_IMAGE};

    public MainTypeDataSource(Context context) {
        dbHelper = new MainTypeDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public MainType createMainType(String name, int image) {
        ContentValues values = new ContentValues();
        values.put(MainTypeDB_SQL.COLUMN_NAME, name);
        values.put(MainTypeDB_SQL.COLUMN_IMAGE, image);
        long insertId = database.insert(MainTypeDB_SQL.TABLE_MAIN, null,
                values);
        Cursor cursor = database.query(MainTypeDB_SQL.TABLE_MAIN,
                allColumns, MainTypeDB_SQL.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        MainType newType = cursorToMainType(cursor);
        cursor.close();
        return newType;
    }

    public void deleteProduct(MainType type) {
        long id = type.getId();
        System.out.println("Type deleted with id: " + id);
        database.delete(MainTypeDB_SQL.TABLE_MAIN, MainTypeDB_SQL.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllItems() {
        database.execSQL("delete from " + MainTypeDB_SQL.TABLE_MAIN);
    }

    public List<MainType> getAllAddedTypes() {
        List<MainType> types = new ArrayList<>();

        Cursor cursor = database.query(MainTypeDB_SQL.TABLE_MAIN,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MainType type = cursorToMainType(cursor);
            types.add(type);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return types;
    }

    private MainType cursorToMainType(Cursor cursor) {
        MainType type = new MainType();
        type.setId(cursor.getLong(0));
        type.setTypeName(cursor.getString(1));
        type.setImage(cursor.getInt(2));
        return type;
    }
}
