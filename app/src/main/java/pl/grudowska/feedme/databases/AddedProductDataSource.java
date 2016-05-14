package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddedProductDataSource {

    private SQLiteDatabase mDatabase;
    private AddedProductDB_SQL dbHelper;
    private String[] productColumns = {AddedProductDB_SQL.COLUMN_ID_PROD,
            AddedProductDB_SQL.COLUMN_DATE, AddedProductDB_SQL.COLUMN_TYPE,
            AddedProductDB_SQL.COLUMN_NAME_PROD, AddedProductDB_SQL.COLUMN_DEF1,
            AddedProductDB_SQL.COLUMN_DEF2, AddedProductDB_SQL.COLUMN_DEF3,
            AddedProductDB_SQL.COLUMN_KCAL, AddedProductDB_SQL.COLUMN_PROTEIN,
            AddedProductDB_SQL.COLUMN_CARBOHYDRATES, AddedProductDB_SQL.COLUMN_FIBER,
            AddedProductDB_SQL.COLUMN_FATS, AddedProductDB_SQL.COLUMN_FATS_SATURATED,
            AddedProductDB_SQL.COLUMN_FATS_MONOUNSATURATED, AddedProductDB_SQL.COLUMN_FATS_OMEGA3,
            AddedProductDB_SQL.COLUMN_FATS_OMEGA6, AddedProductDB_SQL.COLUMN_AMOUNT};

    private String[] statusColumns = {AddedProductDB_SQL.COLUMN_ID_PROD_STATUS,
            AddedProductDB_SQL.COLUMN_DATE_STATUS, AddedProductDB_SQL.COLUMN_NAME_PROD_STATUS,
            AddedProductDB_SQL.COLUMN_STATUS};

    public AddedProductDataSource(Context context) {
        dbHelper = new AddedProductDB_SQL(context);
    }

    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<Product> getAllAddedProducts() {
        List<Product> products = new ArrayList<>();
        Product product;

        Cursor cursor = mDatabase.query(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED,
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

    private Product createAddedProduct(String date, String type, String name, int def1, int def2, int def3, double kcal, double protein,
                                      double carbohydrates, double fiber, double fats, double saturated,
                                      double monounsaturated, double omega3, double omega6, double amount) {
        ContentValues values = new ContentValues();
        values.put(AddedProductDB_SQL.COLUMN_DATE, date);
        values.put(AddedProductDB_SQL.COLUMN_TYPE, type);
        values.put(AddedProductDB_SQL.COLUMN_NAME_PROD, name);
        values.put(AddedProductDB_SQL.COLUMN_DEF1, def1);
        values.put(AddedProductDB_SQL.COLUMN_DEF2, def2);
        values.put(AddedProductDB_SQL.COLUMN_DEF3, def3);
        values.put(AddedProductDB_SQL.COLUMN_KCAL, kcal);
        values.put(AddedProductDB_SQL.COLUMN_PROTEIN, protein);
        values.put(AddedProductDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(AddedProductDB_SQL.COLUMN_FIBER, fiber);
        values.put(AddedProductDB_SQL.COLUMN_FATS, fats);
        values.put(AddedProductDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(AddedProductDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(AddedProductDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(AddedProductDB_SQL.COLUMN_FATS_OMEGA6, omega6);
        values.put(AddedProductDB_SQL.COLUMN_AMOUNT, amount);

        long productId = mDatabase.insert(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED, null, values);
        Cursor cursor = mDatabase.query(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED,
                productColumns, AddedProductDB_SQL.COLUMN_ID_PROD + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();

        return newProduct;
    }

    public Product createSimpleAddedProduct(Product product) {
        Product addedProduct = getProductIfAlreadyAdded(product);
        if (addedProduct == null) {
            addedProduct = createAddedProduct(
                    product.date,
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
            createAddedProductStatus(product.name);
        } else {
            // if product already added do not duplicate, only sum amount with existing
            sumAmountOfAddedProduct(addedProduct, product.amount);
            updateProductStatus(product.name);
        }
        return addedProduct;
    }

    private Product cursorToProduct(Cursor cursor) {

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

    private Product getProductIfAlreadyAdded(Product prod) {
        String selectQuery = "SELECT * FROM " + AddedProductDB_SQL.TABLE_PRODUCTS_ADDED + " WHERE "
                + AddedProductDB_SQL.COLUMN_NAME_PROD + " = '" + prod.name + "'";
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Product product;
        if (cursor.getCount() == 0) {
            product = null;
        } else {
            product = cursorToProduct(cursor);
        }
        cursor.close();
        return (product == null) ? null : product;
    }


    public int editAmountOfAddedProduct(Product product, double amount) {
        ContentValues content = new ContentValues();
        content.put(AddedProductDB_SQL.COLUMN_AMOUNT, amount);
        int tmp = mDatabase.update(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED, content, "_id_prod" + "=?",
                new String[]{String.valueOf(product.id)});

        updateProductStatus(product.name);

        return tmp;
    }

    private int sumAmountOfAddedProduct(Product product, double amount) {
        ContentValues content = new ContentValues();
        content.put(AddedProductDB_SQL.COLUMN_AMOUNT, product.amount + amount);
        int tmp = mDatabase.update(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED, content, "_id_prod" + "=?",
                new String[]{String.valueOf(product.id)});

        return tmp;
    }

    private ProductStatus cursorToProductStatus(Cursor cursor) {

        ProductStatus status = new ProductStatus();
        status.id = cursor.getLong(0);
        status.date = cursor.getString(1);
        status.name = cursor.getString(2);
        status.status = cursor.getInt(3);

        return status;
    }

    private ProductStatus createAddedProductStatus(String productName) {

        ContentValues values = new ContentValues();
        values.put(AddedProductDB_SQL.COLUMN_DATE_STATUS,
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        values.put(AddedProductDB_SQL.COLUMN_NAME_PROD_STATUS, productName);
        values.put(AddedProductDB_SQL.COLUMN_STATUS, ProductStatus.Status.NEW.ordinal());

        long productId = mDatabase.insert(AddedProductDB_SQL.TABLE_PRODUCTS_STATUS, null, values);
        Cursor cursor = mDatabase.query(AddedProductDB_SQL.TABLE_PRODUCTS_STATUS,
                statusColumns, AddedProductDB_SQL.COLUMN_ID_PROD_STATUS + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        ProductStatus newStatus = cursorToProductStatus(cursor);
        cursor.close();

        // Log.d(productName, "NEW");

        return newStatus;
    }

    private int updateProductStatus(String productName) {

        ContentValues values = new ContentValues();
        values.put(AddedProductDB_SQL.COLUMN_DATE_STATUS,
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        values.put(AddedProductDB_SQL.COLUMN_STATUS, ProductStatus.Status.UPDATE.ordinal());

        int tmp = mDatabase.update(AddedProductDB_SQL.TABLE_PRODUCTS_STATUS, values, "product_name_status" + "=?",
                new String[]{String.valueOf(productName)});

        // Log.d(productName, "UPDATED");

        return tmp;
    }

    public void deleteAllAddedProducts() {
        mDatabase.execSQL("delete from " + AddedProductDB_SQL.TABLE_PRODUCTS_ADDED);
        mDatabase.execSQL("delete from " + AddedProductDB_SQL.TABLE_PRODUCTS_STATUS);
    }

    public void deleteAddedProduct(Product product) {
        mDatabase.delete(AddedProductDB_SQL.TABLE_PRODUCTS_ADDED, AddedProductDB_SQL.COLUMN_ID_PROD
                + " = " + product.id, null);
        mDatabase.delete(AddedProductDB_SQL.TABLE_PRODUCTS_STATUS, AddedProductDB_SQL.COLUMN_NAME_PROD_STATUS
                + " = '" + product.name + "'", null);
    }
}