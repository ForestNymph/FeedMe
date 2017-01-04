package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArchivedProductDataSource {

    private SQLiteDatabase mDatabase;
    private ArchivedProductDB_SQL dbHelper;
    private String[] allColumns = {ArchivedProductDB_SQL.COLUMN_ID_PROD,
            ArchivedProductDB_SQL.COLUMN_DATE, ArchivedProductDB_SQL.COLUMN_TYPE,
            ArchivedProductDB_SQL.COLUMN_NAME_PROD, ArchivedProductDB_SQL.COLUMN_DEF1,
            ArchivedProductDB_SQL.COLUMN_DEF2, ArchivedProductDB_SQL.COLUMN_DEF3,
            ArchivedProductDB_SQL.COLUMN_KCAL, ArchivedProductDB_SQL.COLUMN_PROTEIN,
            ArchivedProductDB_SQL.COLUMN_CARBOHYDRATES, ArchivedProductDB_SQL.COLUMN_FIBER,
            ArchivedProductDB_SQL.COLUMN_FATS, ArchivedProductDB_SQL.COLUMN_FATS_SATURATED,
            ArchivedProductDB_SQL.COLUMN_FATS_MONOUNSATURATED, ArchivedProductDB_SQL.COLUMN_FATS_OMEGA3,
            ArchivedProductDB_SQL.COLUMN_FATS_OMEGA6, ArchivedProductDB_SQL.COLUMN_AMOUNT};

    private String[] dateKcalColumns = {ArchivedProductDB_SQL.COLUMN_ID_DATA_KCAL,
            ArchivedProductDB_SQL.COLUMN_DATE_KCAL, ArchivedProductDB_SQL.COLUMN_TOTAL_KCAL,
            ArchivedProductDB_SQL.COLUMN_CONTENT_MAIL};

    public ArchivedProductDataSource(Context context) {
        dbHelper = new ArchivedProductDB_SQL(context);
    }

    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    private Product cursorToArchivedProduct(Cursor cursor) {
        Product product = new Product();

        product.id = cursor.getLong(0);
        product.date = cursor.getString(1);
        product.type = cursor.getString(2);
        product.name = cursor.getString(3);
        product.def1 = cursor.getInt(4);
        product.def2 = cursor.getInt(5);
        product.def3 = cursor.getInt(6);
        product.kcal = cursor.getDouble(7);
        product.protein = cursor.getDouble(8);
        product.carbohydrates = cursor.getDouble(9);
        product.fiber = cursor.getDouble(10);
        product.fats = cursor.getDouble(11);
        product.fatsSaturated = cursor.getDouble(12);
        product.fatsMonounsaturated = cursor.getDouble(13);
        product.omega3 = cursor.getDouble(14);
        product.omega6 = cursor.getDouble(15);
        product.amount = cursor.getDouble(16);

        return product;
    }

    public List<Product> getArchivedProductsByDate(String date) {
        List<Product> archived = new ArrayList<>();
        Product product;

        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED,
                allColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                product = cursorToArchivedProduct(cursor);
                if (product.date.equals(date)) {
                    archived.add(product);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return archived;
    }

    private Product createArchivedProduct(String date, String type, String name,
                                          int def1, int def2, int def3, double kcal, double protein,
                                          double carbohydrates, double fiber, double fats,
                                          double saturated, double monounsaturated,
                                          double omega3, double omega6, double amount) {
        ContentValues values = new ContentValues();
        values.put(ArchivedProductDB_SQL.COLUMN_DATE, date);
        values.put(ArchivedProductDB_SQL.COLUMN_TYPE, type);
        values.put(ArchivedProductDB_SQL.COLUMN_NAME_PROD, name);
        values.put(ArchivedProductDB_SQL.COLUMN_DEF1, def1);
        values.put(ArchivedProductDB_SQL.COLUMN_DEF2, def2);
        values.put(ArchivedProductDB_SQL.COLUMN_DEF3, def3);
        values.put(ArchivedProductDB_SQL.COLUMN_KCAL, kcal);
        values.put(ArchivedProductDB_SQL.COLUMN_PROTEIN, protein);
        values.put(ArchivedProductDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(ArchivedProductDB_SQL.COLUMN_FIBER, fiber);
        values.put(ArchivedProductDB_SQL.COLUMN_FATS, fats);
        values.put(ArchivedProductDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(ArchivedProductDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(ArchivedProductDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(ArchivedProductDB_SQL.COLUMN_FATS_OMEGA6, omega6);
        values.put(ArchivedProductDB_SQL.COLUMN_AMOUNT, amount);

        long productId = mDatabase.insert(ArchivedProductDB_SQL.TABLE_ARCHIVED, null, values);
        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED,
                allColumns, ArchivedProductDB_SQL.COLUMN_ID_PROD + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        Product archived = cursorToArchivedProduct(cursor);
        cursor.close();

        return archived;
    }

    public Product createArchivedProduct(String date, Product product) {
        Product copy = createArchivedProduct(
                date,
                product.type,
                product.name,
                product.def1,
                product.def2,
                product.def3,
                product.kcal,
                product.protein,
                product.carbohydrates,
                product.fiber,
                product.fats,
                product.fatsSaturated,
                product.fatsMonounsaturated,
                product.omega3,
                product.omega6,
                product.amount);
        return copy;
    }

    public void deleteAllArchivedItems() {
        mDatabase.execSQL("delete from " + ArchivedProductDB_SQL.TABLE_ARCHIVED);
        mDatabase.execSQL("delete from " + ArchivedProductDB_SQL.TABLE_ARCHIVED_DATE_KCAL);
    }

    public List<Product> getAllArchivedProducts() {
        List<Product> archived = new ArrayList<>();
        Product archive;

        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED,
                allColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                archive = cursorToArchivedProduct(cursor);
                archived.add(archive);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return archived;
    }

    public List<DailyRecap> getAllArchivedDailyRecaps() {
        List<DailyRecap> recaps = new ArrayList<>();
        DailyRecap recap;
        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED_DATE_KCAL,
                dateKcalColumns, null, null, null, null, null);

        if (cursor.moveToLast()) {
            while (!cursor.isBeforeFirst()) {
                recap = cursorToDailyRecapItem(cursor);
                recaps.add(recap);
                cursor.moveToPrevious();
            }
        }
        cursor.close();
        return recaps;
    }

    public void createDailyRecap(String date, int totalKcal, String contentMail) {
        ContentValues values = new ContentValues();
        values.put(ArchivedProductDB_SQL.COLUMN_DATE_KCAL, date);
        values.put(ArchivedProductDB_SQL.COLUMN_TOTAL_KCAL, totalKcal);
        values.put(ArchivedProductDB_SQL.COLUMN_CONTENT_MAIL, contentMail);

        long recapId = mDatabase.insert(ArchivedProductDB_SQL.TABLE_ARCHIVED_DATE_KCAL, null, values);
        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED_DATE_KCAL,
                dateKcalColumns, ArchivedProductDB_SQL.COLUMN_ID_DATA_KCAL + " = " + recapId, null,
                null, null, null);
        cursor.close();
    }

    private DailyRecap cursorToDailyRecapItem(Cursor cursor) {
        DailyRecap recap = new DailyRecap();

        recap.id = cursor.getLong(0);
        recap.date = cursor.getString(1);
        recap.totalKcal = cursor.getInt(2);
        recap.contentMail = cursor.getString(3);

        return recap;
    }

    public String getContentMailByDate(String date) {
        DailyRecap recap = new DailyRecap();
        Cursor cursor = mDatabase.query(ArchivedProductDB_SQL.TABLE_ARCHIVED_DATE_KCAL,
                dateKcalColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                recap = cursorToDailyRecapItem(cursor);

                if (recap.date.equals(date)) {
                    break;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return recap.contentMail;
    }
}
