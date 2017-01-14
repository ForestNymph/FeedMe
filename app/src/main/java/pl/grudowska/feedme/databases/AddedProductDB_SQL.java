package pl.grudowska.feedme.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AddedProductDB_SQL extends SQLiteOpenHelper {

    static final String TABLE_PRODUCTS_ADDED = "added";

    // Columns in TABLE_PRODUCTS_ADDED
    static final String COLUMN_ID_PROD = "_id_prod";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TYPE = "product_type";
    static final String COLUMN_NAME_PROD = "product_name";
    static final String COLUMN_DEF1 = "def1";
    static final String COLUMN_DEF2 = "def2";
    static final String COLUMN_DEF3 = "def3";
    static final String COLUMN_KCAL = "kcal";
    static final String COLUMN_PROTEIN = "protein";
    static final String COLUMN_CARBOHYDRATES = "carbohydrates";
    static final String COLUMN_FIBER = "fiber";
    static final String COLUMN_FATS = "fats";
    static final String COLUMN_FATS_SATURATED = "fats_saturated";
    static final String COLUMN_FATS_MONOUNSATURATED = "fats_monounsaturated";
    static final String COLUMN_FATS_OMEGA3 = "omega3";
    static final String COLUMN_FATS_OMEGA6 = "omega6";
    static final String COLUMN_AMOUNT = "amount";

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

    AddedProductDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ADDED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS_ADDED);
        onCreate(db);
    }
}
