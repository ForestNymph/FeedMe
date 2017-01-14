package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class RecapInfoHelperDB_SQL extends SQLiteOpenHelper {

    static final String TABLE_TYPE = "types";
    static final String TABLE_SUMMARY = "summaries";

    // Columns in TABLE_TYPE
    static final String COLUMN_ID_TYPE = "_id_type";
    static final String COLUMN_NAME_TYPE = "type";
    static final String COLUMN_IMAGE_TYPE = "image";

    // Columns in TABLE_SUMMARY
    static final String COLUMN_ID_SUMMARY = "_id_summary";
    static final String COLUMN_NAME_SUMMARY = "summary_name";
    static final String COLUMN_MAX_SUMMARY = "summary_max";
    static final String COLUMN_MIN_SUMMARY = "summary_min";

    private static final String DATABASE_NAME = "description.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_TYPE = "create table "
            + TABLE_TYPE + "(" + COLUMN_ID_TYPE
            + " integer primary key autoincrement, " + COLUMN_NAME_TYPE
            + " text not null, " + COLUMN_IMAGE_TYPE + " integer);";

    private static final String CREATE_TABLE_SUMMARY_RANGE = "create table "
            + TABLE_SUMMARY + "(" + COLUMN_ID_SUMMARY
            + " integer primary key autoincrement, " + COLUMN_NAME_SUMMARY
            + " text not null, " + COLUMN_MAX_SUMMARY + " integer, " + COLUMN_MIN_SUMMARY + " integer);";

    RecapInfoHelperDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_SUMMARY_RANGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUMMARY);
        onCreate(db);
    }
}
