package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecapInfoDataSource {

    // Database fields
    private SQLiteDatabase mDatabase;
    private RecapInfoHelperDB_SQL dbHelper;

    private String[] typeColumns = {RecapInfoHelperDB_SQL.COLUMN_ID_TYPE,
            RecapInfoHelperDB_SQL.COLUMN_NAME_TYPE, RecapInfoHelperDB_SQL.COLUMN_IMAGE_TYPE};

    private String[] summaryColumns = {RecapInfoHelperDB_SQL.COLUMN_ID_SUMMARY, RecapInfoHelperDB_SQL.COLUMN_NAME_SUMMARY,
            RecapInfoHelperDB_SQL.COLUMN_MAX_SUMMARY, RecapInfoHelperDB_SQL.COLUMN_MIN_SUMMARY};

    public RecapInfoDataSource(Context context) {
        dbHelper = new RecapInfoHelperDB_SQL(context);
    }

    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SummaryRange createSummaryRange(String name, int max, int min) {
        ContentValues values = new ContentValues();
        values.put(RecapInfoHelperDB_SQL.COLUMN_NAME_SUMMARY, name);
        values.put(RecapInfoHelperDB_SQL.COLUMN_MAX_SUMMARY, max);
        values.put(RecapInfoHelperDB_SQL.COLUMN_MIN_SUMMARY, min);

        long productId = mDatabase.insert(RecapInfoHelperDB_SQL.TABLE_SUMMARY, null, values);
        Cursor cursor = mDatabase.query(RecapInfoHelperDB_SQL.TABLE_SUMMARY,
                summaryColumns, RecapInfoHelperDB_SQL.COLUMN_ID_SUMMARY + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        SummaryRange newSummary = cursorToSummary(cursor);
        cursor.close();

        return newSummary;
    }

    public ProductType createType(String name, int image) {
        ContentValues values = new ContentValues();
        values.put(RecapInfoHelperDB_SQL.COLUMN_NAME_TYPE, name);
        values.put(RecapInfoHelperDB_SQL.COLUMN_IMAGE_TYPE, image);

        long insertId = mDatabase.insert(RecapInfoHelperDB_SQL.TABLE_TYPE, null, values);
        Cursor cursor = mDatabase.query(RecapInfoHelperDB_SQL.TABLE_TYPE,
                typeColumns, RecapInfoHelperDB_SQL.COLUMN_ID_TYPE + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ProductType newType = cursorToType(cursor);
        cursor.close();
        return newType;
    }

    public List<ProductType> getTypesAllProducts() {
        List<ProductType> types = new ArrayList<>();
        ProductType type;

        Cursor cursor = mDatabase.query(RecapInfoHelperDB_SQL.TABLE_TYPE,
                typeColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                type = cursorToType(cursor);
                types.add(type);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return types;
    }

    private ProductType cursorToType(Cursor cursor) {
        ProductType type = new ProductType();
        type.id = cursor.getLong(0);
        type.typeName = cursor.getString(1);
        type.resImage = cursor.getInt(2);

        return type;
    }

    public void deleteAllTypes() {
        mDatabase.execSQL("delete from " + RecapInfoHelperDB_SQL.TABLE_TYPE);
    }

    public List<SummaryRange> getAllSummaries() {
        List<SummaryRange> summaries = new ArrayList<>();
        SummaryRange summary;

        Cursor cursor = mDatabase.query(RecapInfoHelperDB_SQL.TABLE_SUMMARY,
                summaryColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                summary = cursorToSummary(cursor);
                summaries.add(summary);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return summaries;
    }

    private SummaryRange cursorToSummary(Cursor cursor) {
        SummaryRange summary = new SummaryRange();
        summary.id = cursor.getLong(0);
        summary.typeName = cursor.getString(1);
        summary.maxRange = cursor.getInt(2);
        summary.minRange = cursor.getInt(3);

        return summary;
    }

    public void deleteAllSummaries() {
        mDatabase.execSQL("delete from " + RecapInfoHelperDB_SQL.TABLE_SUMMARY);
    }
}