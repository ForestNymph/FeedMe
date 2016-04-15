package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductHelperDB_SQL extends SQLiteOpenHelper {

    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_TYPE = "types";
    public static final String TABLE_PRODUCT_TYPE = "products_type";
    public static final String TABLE_PRODUCT_ADDED = "products_added";

    // Columns in TABLE_PRODUCTS
    public static final String COLUMN_ID_PROD = "_id_prod";
    public static final String COLUMN_TYPE = "product_type";
    public static final String COLUMN_NAME_PROD = "product_name";
    public static final String COLUMN_DEF1 = "def1";
    public static final String COLUMN_DEF2 = "def2";
    public static final String COLUMN_DEF3 = "def3";
    public static final String COLUMN_KCAL = "kcal";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_CARBOHYDRATES = "carbohydrates";
    public static final String COLUMN_FIBER = "fiber";
    public static final String COLUMN_FATS = "fats";
    public static final String COLUMN_FATS_SATURATED = "fats_saturated";
    public static final String COLUMN_FATS_MONOUNSATURATED = "fats_monounsaturated";
    public static final String COLUMN_FATS_OMEGA3 = "omega3";
    public static final String COLUMN_FATS_OMEGA6 = "omega6";
    public static final String COLUMN_AMOUNT = "amount";

    // Columns in TABLE_TYPE
    public static final String COLUMN_ID_TYPE = "_id_type";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_IMAGE_TYPE = "image";
    // Column in TABLE_PRODUCT_TYPE
    public static final String COLUMN_ID_PRODUCT_TYPE = "_id_product_type";
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_TYPE = "create table "
            + TABLE_TYPE + "(" + COLUMN_ID_TYPE
            + " integer primary key autoincrement, " + COLUMN_NAME_TYPE
            + " text not null, " + COLUMN_IMAGE_TYPE + " integer);";

    private static final String CREATE_TABLE_PRODUCT = "create table "
            + TABLE_PRODUCT + "(" + COLUMN_ID_PROD
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " text not null, " + COLUMN_NAME_PROD + " text not null, " + COLUMN_DEF1 + " integer, "
            + COLUMN_DEF2 + " integer, " + COLUMN_DEF3 + " integer, " + COLUMN_KCAL + " double, "
            + COLUMN_PROTEIN + " double, " + COLUMN_CARBOHYDRATES + " double, " + COLUMN_FIBER + " double, "
            + COLUMN_FATS + " double, " + COLUMN_FATS_SATURATED + " double, "
            + COLUMN_FATS_MONOUNSATURATED + " double, " + COLUMN_FATS_OMEGA3 + " double, "
            + COLUMN_FATS_OMEGA6 + " double, " + COLUMN_AMOUNT + " double);";

    private static final String CREATE_TABLE_PRODUCT_TYPE = "create table "
            + TABLE_PRODUCT_TYPE + "(" + COLUMN_ID_PRODUCT_TYPE + " integer primary key autoincrement, "
            + COLUMN_ID_PROD + " integer, " + COLUMN_ID_TYPE + " integer);";

    private static final String CREATE_TABLE_PRODUCT_ADDED = "create table "
            + TABLE_PRODUCT_ADDED + "(" + COLUMN_ID_PROD
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " text not null, " + COLUMN_NAME_PROD + " text not null, " + COLUMN_DEF1 + " integer, "
            + COLUMN_DEF2 + " integer, " + COLUMN_DEF3 + " integer, " + COLUMN_KCAL + " double, "
            + COLUMN_PROTEIN + " double, " + COLUMN_CARBOHYDRATES + " double, " + COLUMN_FIBER + " double, "
            + COLUMN_FATS + " double, " + COLUMN_FATS_SATURATED + " double, "
            + COLUMN_FATS_MONOUNSATURATED + " double, " + COLUMN_FATS_OMEGA3 + " double, "
            + COLUMN_FATS_OMEGA6 + " double, " + COLUMN_AMOUNT + " double);";

    public ProductHelperDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT_TYPE);
        db.execSQL(CREATE_TABLE_PRODUCT_ADDED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_ADDED);
        // create new tables
        onCreate(db);
    }
}