package pl.grudowska.feedme.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ProductHelperDB_SQL dbHelper;

    private String[] productColumns = {ProductHelperDB_SQL.COLUMN_ID_PROD,
            ProductHelperDB_SQL.COLUMN_TYPE, ProductHelperDB_SQL.COLUMN_NAME_PROD, ProductHelperDB_SQL.COLUMN_DEF1,
            ProductHelperDB_SQL.COLUMN_DEF2, ProductHelperDB_SQL.COLUMN_DEF3, ProductHelperDB_SQL.COLUMN_KCAL,
            ProductHelperDB_SQL.COLUMN_PROTEIN, ProductHelperDB_SQL.COLUMN_CARBOHYDRATES, ProductHelperDB_SQL.COLUMN_ROUGHAGE,
            ProductHelperDB_SQL.COLUMN_FATS, ProductHelperDB_SQL.COLUMN_FATS_SATURATED, ProductHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED,
            ProductHelperDB_SQL.COLUMN_FATS_OMEGA3, ProductHelperDB_SQL.COLUMN_FATS_OMEGA6, ProductHelperDB_SQL.COLUMN_AMOUNT};

    private String[] productColumnsAdded = {ProductHelperDB_SQL.COLUMN_ID_PROD,
            ProductHelperDB_SQL.COLUMN_TYPE, ProductHelperDB_SQL.COLUMN_NAME_PROD, ProductHelperDB_SQL.COLUMN_DEF1,
            ProductHelperDB_SQL.COLUMN_DEF2, ProductHelperDB_SQL.COLUMN_DEF3, ProductHelperDB_SQL.COLUMN_KCAL,
            ProductHelperDB_SQL.COLUMN_PROTEIN, ProductHelperDB_SQL.COLUMN_CARBOHYDRATES, ProductHelperDB_SQL.COLUMN_ROUGHAGE,
            ProductHelperDB_SQL.COLUMN_FATS, ProductHelperDB_SQL.COLUMN_FATS_SATURATED, ProductHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED,
            ProductHelperDB_SQL.COLUMN_FATS_OMEGA3, ProductHelperDB_SQL.COLUMN_FATS_OMEGA6, ProductHelperDB_SQL.COLUMN_AMOUNT};

    private String[] typeColumns = {ProductHelperDB_SQL.COLUMN_ID_TYPE,
            ProductHelperDB_SQL.COLUMN_NAME_TYPE, ProductHelperDB_SQL.COLUMN_IMAGE_TYPE};

    public ProductDataSource(Context context) {
        dbHelper = new ProductHelperDB_SQL(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Product createSimpleAddedProduct(Product product) {
        Product copy = createAddedProduct(
                product.getType(),
                product.getName(),
                product.getDef1(),
                product.getDef2(),
                product.getDef3(),
                product.getKcal(),
                product.getProtein(),
                product.getCarbohydrates(),
                product.getRoughage(),
                product.getFats(),
                product.getFatsSaturated(),
                product.getFatsMonounsaturated(),
                product.getOmega3(),
                product.getOmega6(),
                product.getAmount());
        return copy;
    }

    public Product createAddedProduct(String type, String name, int def1, int def2, int def3, double kcal, double protein,
                                      double carbohydrates, double roughage, double fats, double saturated,
                                      double monounsaturated, double omega3, double omega6, double amount) {
        ContentValues values = new ContentValues();
        values.put(ProductHelperDB_SQL.COLUMN_TYPE, type);
        values.put(ProductHelperDB_SQL.COLUMN_NAME_PROD, name);
        values.put(ProductHelperDB_SQL.COLUMN_DEF1, def1);
        values.put(ProductHelperDB_SQL.COLUMN_DEF2, def2);
        values.put(ProductHelperDB_SQL.COLUMN_DEF3, def3);
        values.put(ProductHelperDB_SQL.COLUMN_KCAL, kcal);
        values.put(ProductHelperDB_SQL.COLUMN_PROTEIN, protein);
        values.put(ProductHelperDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(ProductHelperDB_SQL.COLUMN_ROUGHAGE, roughage);
        values.put(ProductHelperDB_SQL.COLUMN_FATS, fats);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_OMEGA6, omega6);
        values.put(ProductHelperDB_SQL.COLUMN_AMOUNT, amount);

        long productId = database.insert(ProductHelperDB_SQL.TABLE_PRODUCT_ADDED, null, values);
        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_PRODUCT_ADDED,
                productColumnsAdded, ProductHelperDB_SQL.COLUMN_ID_PROD + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();

        // Connect Product with Type
        // createProductType(productId, findTypeID(newProduct.getType()));

        return newProduct;
    }

    public Product createProduct(String type, String name, int def1, int def2, int def3, double kcal, double protein,
                                 double carbohydrates, double roughage, double fats, double saturated,
                                 double monounsaturated, double omega3, double omega6, double amount) {
        ContentValues values = new ContentValues();
        values.put(ProductHelperDB_SQL.COLUMN_TYPE, type);
        values.put(ProductHelperDB_SQL.COLUMN_NAME_PROD, name);
        values.put(ProductHelperDB_SQL.COLUMN_DEF1, def1);
        values.put(ProductHelperDB_SQL.COLUMN_DEF2, def2);
        values.put(ProductHelperDB_SQL.COLUMN_DEF3, def3);
        values.put(ProductHelperDB_SQL.COLUMN_KCAL, kcal);
        values.put(ProductHelperDB_SQL.COLUMN_PROTEIN, protein);
        values.put(ProductHelperDB_SQL.COLUMN_CARBOHYDRATES, carbohydrates);
        values.put(ProductHelperDB_SQL.COLUMN_ROUGHAGE, roughage);
        values.put(ProductHelperDB_SQL.COLUMN_FATS, fats);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_SATURATED, saturated);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_MONOUNSATURATED, monounsaturated);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_OMEGA3, omega3);
        values.put(ProductHelperDB_SQL.COLUMN_FATS_OMEGA6, omega6);
        values.put(ProductHelperDB_SQL.COLUMN_AMOUNT, amount);

        long productId = database.insert(ProductHelperDB_SQL.TABLE_PRODUCT, null, values);
        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_PRODUCT,
                productColumns, ProductHelperDB_SQL.COLUMN_ID_PROD + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();

        // Connect Product with Type
        // createProductType(productId, findTypeID(newProduct.getType()));

        return newProduct;
    }

    private long findTypeID(String typeName) {
        List<ProductType> types = getAllTypes();

        for (ProductType type : types) {
            if (type.getTypeName().equals(typeName)) {
                return type.getId();
            }
        }
        return 0;
    }

    public ProductType createType(String name, int image) {
        ContentValues values = new ContentValues();
        values.put(ProductHelperDB_SQL.COLUMN_NAME_TYPE, name);
        values.put(ProductHelperDB_SQL.COLUMN_IMAGE_TYPE, image);

        long insertId = database.insert(ProductHelperDB_SQL.TABLE_TYPE, null, values);
        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_TYPE,
                typeColumns, ProductHelperDB_SQL.COLUMN_ID_TYPE + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        ProductType newType = cursorToType(cursor);
        cursor.close();
        return newType;
    }

    private long createProductType(long productID, long typeID) {
        ContentValues values = new ContentValues();
        values.put(ProductHelperDB_SQL.COLUMN_ID_PROD, productID);
        values.put(ProductHelperDB_SQL.COLUMN_ID_TYPE, typeID);
        return database.insert(ProductHelperDB_SQL.TABLE_PRODUCT_TYPE, null, values);
    }

    public List<Product> getProductsByType(String typeName) {
        List<Product> products = new ArrayList<>();
        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_PRODUCT,
                productColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            if (product.getType().equals(typeName)) {
                products.add(product);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }

    public Product getProduct(long productID) {
        String selectQuery = "SELECT  * FROM " + ProductHelperDB_SQL.TABLE_PRODUCT + " WHERE "
                + ProductHelperDB_SQL.COLUMN_ID_PROD + " = " + productID;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Product product;
        while (!cursor.isAfterLast()) {
            product = cursorToProduct(cursor);
            if (product.getId() == productID) {
                cursor.close();
                return product;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return null;
    }

    public List<ProductType> getAllTypes() {
        List<ProductType> types = new ArrayList<>();

        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_TYPE,
                typeColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProductType type = cursorToType(cursor);
            types.add(type);
            cursor.moveToNext();
        }
        cursor.close();
        return types;
    }

    public List<Product> getAllAddedProducts() {
        List<Product> products = new ArrayList<>();

        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_PRODUCT_ADDED,
                productColumnsAdded, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        Cursor cursor = database.query(ProductHelperDB_SQL.TABLE_PRODUCT,
                productColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }

    public void deleteAll() {
        database.execSQL("delete from " + ProductHelperDB_SQL.TABLE_PRODUCT);
        database.execSQL("delete from " + ProductHelperDB_SQL.TABLE_TYPE);
        database.execSQL("delete from " + ProductHelperDB_SQL.TABLE_PRODUCT_TYPE);
        database.execSQL("delete from " + ProductHelperDB_SQL.TABLE_PRODUCT_ADDED);
    }

    public void deleteAllAddedProducts() {
        database.execSQL("delete from " + ProductHelperDB_SQL.TABLE_PRODUCT_ADDED);
    }

    public void deleteAddedProduct(Product product) {
        database.delete(ProductHelperDB_SQL.TABLE_PRODUCT_ADDED, ProductHelperDB_SQL.COLUMN_ID_PROD
                + " = " + product.getId(), null);
    }

    private Product cursorToProduct(Cursor cursor) {
        Product type = new Product();
        type.setId(cursor.getLong(0));
        type.setType(cursor.getString(1));
        type.setName(cursor.getString(2));
        type.setDef1(cursor.getInt(3));
        type.setDef2(cursor.getInt(4));
        type.setDef3(cursor.getInt(5));
        type.setKcal(cursor.getDouble(6));
        type.setProtein(cursor.getDouble(7));
        type.setCarbohydrates(cursor.getDouble(8));
        type.setRoughage(cursor.getDouble(9));
        type.setFats(cursor.getDouble(10));
        type.setFatsSaturated(cursor.getDouble(11));
        type.setFatsMonounsaturated(cursor.getDouble(12));
        type.setOmega3(cursor.getDouble(13));
        type.setOmega6(cursor.getDouble(14));
        type.setAmount(cursor.getDouble(15));

        return type;
    }

    private ProductType cursorToType(Cursor cursor) {
        ProductType type = new ProductType();
        type.setId(cursor.getLong(0));
        type.setTypeName(cursor.getString(1));
        type.setImage(cursor.getInt(2));

        return type;
    }
}