package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class ProductDataSource extends SQLiteOpenHelper {

    public static final String TABLE_PRODUCT = "TABLE_PRODUCT";

    public static final String COLUMN_ID_PROD = "COLUMN_ID_PROD";
    public static final String COLUMN_DATE = "COLUMN_DATE";
    public static final String COLUMN_TYPE = "COLUMN_TYPE";
    public static final String COLUMN_NAME_PROD = "COLUMN_NAME_PROD";
    public static final String COLUMN_DEF1 = "COLUMN_DEF1";
    public static final String COLUMN_DEF2 = "COLUMN_DEF2";
    public static final String COLUMN_DEF3 = "COLUMN_DEF3";
    public static final String COLUMN_KCAL = "COLUMN_KCAL";
    public static final String COLUMN_PROTEIN = "COLUMN_PROTEIN";
    public static final String COLUMN_CARBOHYDRATES = "COLUMN_CARBOHYDRATES";
    public static final String COLUMN_FIBER = "COLUMN_FIBER";
    public static final String COLUMN_FATS = "COLUMN_FATS";
    public static final String COLUMN_FATS_SATURATED = "COLUMN_FATS_SATURATED";
    public static final String COLUMN_FATS_MONOUNSATURATED = "COLUMN_FATS_MONOUNSATURATED";
    public static final String COLUMN_FATS_OMEGA3 = "COLUMN_FATS_OMEGA3";
    public static final String COLUMN_FATS_OMEGA6 = "COLUMN_FATS_OMEGA6";
    public static final String COLUMN_AMOUNT = "COLUMN_AMOUNT";

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH;

    private String[] productColumns = {COLUMN_ID_PROD, COLUMN_DATE,
            COLUMN_TYPE, COLUMN_NAME_PROD, COLUMN_DEF1,
            COLUMN_DEF2, COLUMN_DEF3, COLUMN_KCAL,
            COLUMN_PROTEIN, COLUMN_CARBOHYDRATES, COLUMN_FIBER,
            COLUMN_FATS, COLUMN_FATS_SATURATED, COLUMN_FATS_MONOUNSATURATED,
            COLUMN_FATS_OMEGA3, COLUMN_FATS_OMEGA6, COLUMN_AMOUNT};
    private SQLiteDatabase mDataBase;

    public ProductDataSource(Context context) {
        super(context, getDatabaseName(context), null, DATABASE_VERSION);

        DATABASE_PATH = context.getDatabasePath(getDatabaseName(context)).toString();
    }

    public static String getDatabaseAdress(Context context) {
        return SharedPreferencesManager.loadDataString(context, "serverAddress", "http://192.168.1.144:8090/files/");
    }

    public static int getPortNumber(Context context) {
        return SharedPreferencesManager.loadDataInt(context, "serverPort", 8090);
    }

    public static String getDatabaseName(Context context) {
        return SharedPreferencesManager.loadDataString(context, "databaseName", "products.db");
    }

    private boolean checkDBifExists() {
        File dbFile = new File(DATABASE_PATH);
        return dbFile.exists();
    }

    public boolean openDataBase() throws DatabaseNotExistException {
        if (!checkDBifExists()) {
            throw new DatabaseNotExistException("Database products.db don't exists");
        }
        mDataBase = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<Product> getProductsByType(String typeName) {
        ArrayList<Product> products = new ArrayList<>();
        Product product;

        Cursor cursor = mDataBase.query(TABLE_PRODUCT, productColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                product = cursorToProduct(cursor);
                if (product.type.equals(typeName)) {
                    products.add(product);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return products;
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

    public Product getProduct(long productID) {
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "
                + COLUMN_ID_PROD + " = " + productID;
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        Product product = cursorToProduct(cursor);
        cursor.close();

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Product product;

        Cursor cursor = mDataBase.query(TABLE_PRODUCT,
                productColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                product = cursorToProduct(cursor);
                products.add(product);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return products;
    }

    public class DatabaseNotExistException extends Exception {
        public DatabaseNotExistException(String message) {
            super(message);
        }
    }
}
