package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SupplementaryInfoDataSource {

    // Database fields
    private SQLiteDatabase mDatabase;
    private SupplementaryInfoHelperDB_SQL dbHelper;

    private String[] productColumns = {SupplementaryInfoHelperDB_SQL.COLUMN_ID_PROD,
            SupplementaryInfoHelperDB_SQL.COLUMN_TYPE, SupplementaryInfoHelperDB_SQL.COLUMN_NAME_PROD, SupplementaryInfoHelperDB_SQL.COLUMN_DEF1,
            SupplementaryInfoHelperDB_SQL.COLUMN_DEF2, SupplementaryInfoHelperDB_SQL.COLUMN_DEF3, SupplementaryInfoHelperDB_SQL.COLUMN_KCAL,
            SupplementaryInfoHelperDB_SQL.COLUMN_PROTEIN, SupplementaryInfoHelperDB_SQL.COLUMN_CARBOHYDRATES, SupplementaryInfoHelperDB_SQL.COLUMN_FIBER,
            SupplementaryInfoHelperDB_SQL.COLUMN_FATS, SupplementaryInfoHelperDB_SQL.COLUMN_FATS_SATURATED, SupplementaryInfoHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED,
            SupplementaryInfoHelperDB_SQL.COLUMN_FATS_OMEGA3, SupplementaryInfoHelperDB_SQL.COLUMN_FATS_OMEGA6, SupplementaryInfoHelperDB_SQL.COLUMN_AMOUNT};

    private String[] typeColumns = {SupplementaryInfoHelperDB_SQL.COLUMN_ID_TYPE,
            SupplementaryInfoHelperDB_SQL.COLUMN_NAME_TYPE, SupplementaryInfoHelperDB_SQL.COLUMN_IMAGE_TYPE};

    private String[] summaryColumns = {SupplementaryInfoHelperDB_SQL.COLUMN_ID_SUMMARY, SupplementaryInfoHelperDB_SQL.COLUMN_NAME_SUMMARY,
            SupplementaryInfoHelperDB_SQL.COLUMN_MAX_SUMMARY, SupplementaryInfoHelperDB_SQL.COLUMN_MIN_SUMMARY};

    public SupplementaryInfoDataSource(Context context) {
        dbHelper = new SupplementaryInfoHelperDB_SQL(context);
    }

    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Product createSimpleAddedProduct(ArchivedProduct product) {
        Product copy = createAddedProduct(
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

    public Product createSimpleAddedProduct(Product product) {
        Product copy = createAddedProduct(
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

    public SummaryRange createSummaryRange(String name, int max, int min) {
        ContentValues values = new ContentValues();
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_NAME_SUMMARY, name);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_MAX_SUMMARY, max);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_MIN_SUMMARY, min);

        long productId = mDatabase.insert(SupplementaryInfoHelperDB_SQL.TABLE_SUMMARY, null, values);
        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_SUMMARY,
                summaryColumns, SupplementaryInfoHelperDB_SQL.COLUMN_ID_SUMMARY + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        SummaryRange newSummary = cursorToSummary(cursor);
        cursor.close();

        return newSummary;
    }

    public Product createAddedProduct(String type, String name, int def1, int def2, int def3, double kcal, double protein,
                                      double carbohydrates, double fiber, double fats, double saturated,
                                      double monounsaturated, double omega3, double omega6, double amount) {
        ContentValues values = new ContentValues();
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_TYPE, type);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_NAME_PROD, name);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_DEF1, def1);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_DEF2, def2);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_DEF3, def3);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_KCAL, kcal);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_PROTEIN, protein);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FIBER, fiber);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FATS, fats);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_FATS_OMEGA6, omega6);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_AMOUNT, amount);

        long productId = mDatabase.insert(SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED, null, values);
        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED,
                productColumns, SupplementaryInfoHelperDB_SQL.COLUMN_ID_PROD + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();

        return newProduct;
    }

    public int modifyAmountOfAddedProduct(Product product, double amount1) {
        ContentValues content = new ContentValues();
        content.put(SupplementaryInfoHelperDB_SQL.COLUMN_AMOUNT, amount1);
        int tmp = mDatabase.update(SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED, content, "_id_prod" + "=?",
                new String []{String.valueOf(product.id)});
        return tmp;
    }

    public ProductType createType(String name, int image) {
        ContentValues values = new ContentValues();
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_NAME_TYPE, name);
        values.put(SupplementaryInfoHelperDB_SQL.COLUMN_IMAGE_TYPE, image);

        long insertId = mDatabase.insert(SupplementaryInfoHelperDB_SQL.TABLE_TYPE, null, values);
        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_TYPE,
                typeColumns, SupplementaryInfoHelperDB_SQL.COLUMN_ID_TYPE + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        ProductType newType = cursorToType(cursor);
        cursor.close();
        return newType;
    }

    public List<ProductType> getTypesAllProducts() {
        List<ProductType> types = new ArrayList<>();
        ProductType type;

        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_TYPE,
                typeColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            type = cursorToType(cursor);
            types.add(type);
            cursor.moveToNext();
        }
        cursor.close();
        return types;
    }

    public List<Product> getAllAddedProducts() {
        List<Product> products = new ArrayList<>();
        Product product;

        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED,
                productColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }

    public List<SummaryRange> getAllSummaries() {
        List<SummaryRange> summaries = new ArrayList<>();
        SummaryRange summary;

        Cursor cursor = mDatabase.query(SupplementaryInfoHelperDB_SQL.TABLE_SUMMARY,
                summaryColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            summary = cursorToSummary(cursor);
            summaries.add(summary);
            cursor.moveToNext();
        }
        cursor.close();
        return summaries;
    }

    public void deleteAllTypes() {
        mDatabase.execSQL("delete from " + SupplementaryInfoHelperDB_SQL.TABLE_TYPE);
    }

    public void deleteAllSummaries() {
        mDatabase.execSQL("delete from " + SupplementaryInfoHelperDB_SQL.TABLE_SUMMARY);
    }

    public void deleteAllAddedProducts() {
        mDatabase.execSQL("delete from " + SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED);
    }

    public void deleteAddedProduct(Product product) {
        mDatabase.delete(SupplementaryInfoHelperDB_SQL.TABLE_PRODUCT_ADDED, SupplementaryInfoHelperDB_SQL.COLUMN_ID_PROD
                + " = " + product.id, null);
    }

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.id = cursor.getLong(0);
        product.type = cursor.getString(1);
        product.name = cursor.getString(2);
        product.def1 = cursor.getInt(3);
        product.def2 = cursor.getInt(4);
        product.def3 = cursor.getInt(5);
        product.kcal = cursor.getDouble(6);
        product.protein = cursor.getDouble(7);
        product.carbohydrates = cursor.getDouble(8);
        product.fiber = cursor.getDouble(9);
        product.fats = cursor.getDouble(10);
        product.fatsSaturated = cursor.getDouble(11);
        product.fatsMonounsaturated = cursor.getDouble(12);
        product.omega3 = cursor.getDouble(13);
        product.omega6 = cursor.getDouble(14);
        product.amount = cursor.getDouble(15);

        return product;
    }

    private SummaryRange cursorToSummary(Cursor cursor) {
        SummaryRange summary = new SummaryRange();
        summary.id = cursor.getLong(0);
        summary.typeName = cursor.getString(1);
        summary.maxRange = cursor.getInt(2);
        summary.minRange = cursor.getInt(3);

        return summary;
    }

    private ProductType cursorToType(Cursor cursor) {
        ProductType type = new ProductType();
        type.id = cursor.getLong(0);
        type.typeName = cursor.getString(1);
        type.resImage = cursor.getInt(2);

        return type;
    }
}