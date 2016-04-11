package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SpecificFoodDataSource {

    // Database fields
    private SQLiteDatabase database;
    private SpecificFoodDB_SQL dbHelper;
    private String[] allColumns = {SpecificFoodDB_SQL.COLUMN_ID,
            SpecificFoodDB_SQL.COLUMN_TYPE_NAME, SpecificFoodDB_SQL.COLUMN_DEF1,
            SpecificFoodDB_SQL.COLUMN_DEF2, SpecificFoodDB_SQL.COLUMN_DEF3, SpecificFoodDB_SQL.COLUMN_KCAL,
            SpecificFoodDB_SQL.COLUMN_PROTEIN, SpecificFoodDB_SQL.COLUMN_CARBOHYDRATES, SpecificFoodDB_SQL.COLUMN_ROUGHAGE,
            SpecificFoodDB_SQL.COLUMN_FATS_UNSATURATED, SpecificFoodDB_SQL.COLUMN_FATS_SATURATED, SpecificFoodDB_SQL.COLUMN_FATS_MONOUNSATURATED,
            SpecificFoodDB_SQL.COLUMN_FATS_OMEGA3, SpecificFoodDB_SQL.COLUMN_FATS_OMEGA6};

    public SpecificFoodDataSource(Context context) {
        dbHelper = new SpecificFoodDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SpecificFood createFullProduct(String name, int def1, int def2, int def3, double kcal, double protein,
                                          double carbohydrates, double roughage, double unsaturated, double saturated,
                                          double monounsaturated, double omega3, double omega6) {
        ContentValues values = new ContentValues();
        values.put(SpecificFoodDB_SQL.COLUMN_TYPE_NAME, name);
        values.put(SpecificFoodDB_SQL.COLUMN_DEF1, def1);
        values.put(SpecificFoodDB_SQL.COLUMN_DEF2, def2);
        values.put(SpecificFoodDB_SQL.COLUMN_DEF3, def3);
        values.put(SpecificFoodDB_SQL.COLUMN_KCAL, kcal);
        values.put(SpecificFoodDB_SQL.COLUMN_PROTEIN, protein);
        values.put(SpecificFoodDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(SpecificFoodDB_SQL.COLUMN_ROUGHAGE, roughage);
        values.put(SpecificFoodDB_SQL.COLUMN_FATS_UNSATURATED, unsaturated);
        values.put(SpecificFoodDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(SpecificFoodDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(SpecificFoodDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(SpecificFoodDB_SQL.COLUMN_FATS_OMEGA6, omega6);

        long insertId = database.insert(SpecificFoodDB_SQL.TABLE_SPECIFIC, null, values);
        Cursor cursor = database.query(SpecificFoodDB_SQL.TABLE_SPECIFIC,
                allColumns, SpecificFoodDB_SQL.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        SpecificFood newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
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

    public List<SpecificFood> getAllAddedTypes() {
        List<SpecificFood> types = new ArrayList<>();

        Cursor cursor = database.query(SpecificFoodDB_SQL.TABLE_SPECIFIC,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SpecificFood type = cursorToProduct(cursor);
            types.add(type);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return types;
    }

    // set data types in columns
    private SpecificFood cursorToProduct(Cursor cursor) {

        SpecificFood type = new SpecificFood();
        type.setId(cursor.getLong(0));
        type.setTypeName(cursor.getString(1));
        type.setDef1(cursor.getInt(2));
        type.setDef2(cursor.getInt(3));
        type.setDef3(cursor.getInt(4));
        type.setKcal(cursor.getDouble(5));
        type.setProtein(cursor.getDouble(6));
        type.setCarbohydrates(cursor.getDouble(7));
        type.setRoughage(cursor.getDouble(8));
        type.setFatsUnsaturated(cursor.getDouble(9));
        type.setFatsSaturated(cursor.getDouble(10));
        type.setFatsMonounsaturated(cursor.getDouble(11));
        type.setOmega3(cursor.getDouble(12));
        type.setOmega6(cursor.getDouble(13));

        return type;
    }
}
