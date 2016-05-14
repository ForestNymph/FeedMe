package pl.grudowska.feedme.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddedProductDB_SQL extends SQLiteOpenHelper {

    public static final String TABLE_PRODUCTS_ADDED = "added";
    public static final String TABLE_PRODUCTS_STATUS = "status";

    // Columns in TABLE_PRODUCTS_ADDED
    public static final String COLUMN_ID_PROD = "_id_prod";
    public static final String COLUMN_DATE = "date";
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

    // Columns in TABLE_PRODUCTS_STATUS
    public static final String COLUMN_ID_PROD_STATUS = "_id_prod_status";
    public static final String COLUMN_DATE_STATUS = "date_status";
    public static final String COLUMN_STATUS = "product_status";
    public static final String COLUMN_NAME_PROD_STATUS = "product_name_status";

    private static final String DATABASE_NAME = "added.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ADDED = "create table "
            + TABLE_PRODUCTS_ADDED + "(" + COLUMN_ID_PROD
            + " integer primary key autoincrement, " + COLUMN_DATE + " text not null, " + COLUMN_TYPE
            + " text not null, " + COLUMN_NAME_PROD + " text not null, " + COLUMN_DEF1 + " integer, "
            + COLUMN_DEF2 + " integer, " + COLUMN_DEF3 + " integer, " + COLUMN_KCAL + " double, "
            + COLUMN_PROTEIN + " double, " + COLUMN_CARBOHYDRATES + " double, " + COLUMN_FIBER + " double, "
            + COLUMN_FATS + " double, " + COLUMN_FATS_SATURATED + " double, "
            + COLUMN_FATS_MONOUNSATURATED + " double, " + COLUMN_FATS_OMEGA3 + " double, "
            + COLUMN_FATS_OMEGA6 + " double, " + COLUMN_AMOUNT + " double);";

    private static final String CREATE_TABLE_STATUS = "create table "
            + TABLE_PRODUCTS_STATUS + "(" + COLUMN_ID_PROD_STATUS + " integer primary key autoincrement, "
            + COLUMN_DATE_STATUS + " text not null, " + COLUMN_STATUS + " integer, " + COLUMN_NAME_PROD_STATUS
            + " text not null);";

    public AddedProductDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ADDED);
        db.execSQL(CREATE_TABLE_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS_ADDED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS_STATUS);
        onCreate(db);
    }
}
