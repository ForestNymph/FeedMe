package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SpecificFoodDB_SQL extends SQLiteOpenHelper {

    public static final String TABLE_SPECIFIC = "all_products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "product_type";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_DEF1 = "def1";
    public static final String COLUMN_DEF2 = "def2";
    public static final String COLUMN_DEF3 = "def3";
    public static final String COLUMN_KCAL = "kcal";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_CARBOHYDRATES = "carbohydrates";
    public static final String COLUMN_ROUGHAGE = "roughage";
    public static final String COLUMN_FATS_UNSATURATED = "fats_unsaturated";
    public static final String COLUMN_FATS_SATURATED = "fats_saturated";
    public static final String COLUMN_FATS_MONOUNSATURATED = "fats_monounsaturated";
    public static final String COLUMN_FATS_OMEGA3 = "omega3";
    public static final String COLUMN_FATS_OMEGA6 = "omega6";

    private static final String DATABASE_NAME = "all_products.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SPECIFIC + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " text not null, " + COLUMN_NAME + " text not null, " + COLUMN_DEF1 + " integer, " + COLUMN_DEF2 + " integer, "
            + COLUMN_DEF3 + " integer, " + COLUMN_KCAL + " double, " + COLUMN_PROTEIN + " double, "
            + COLUMN_CARBOHYDRATES + " double, " + COLUMN_ROUGHAGE + " double, "
            + COLUMN_FATS_UNSATURATED + " double, " + COLUMN_FATS_SATURATED + " double, "
            + COLUMN_FATS_MONOUNSATURATED + " double, " + COLUMN_FATS_OMEGA3 + " double, "
            + COLUMN_FATS_OMEGA6 + " double);";

    public SpecificFoodDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SpecificFoodDB_SQL.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIFIC);
        onCreate(db);
    }
}
