package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecentlyAddedDB_SQL extends SQLiteOpenHelper {

    public static final String TABLE_RECENTLY = "recentlyadded";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product_added";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String DATABASE_NAME = "recentlyadded.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECENTLY + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PRODUCT
            + " text not null, " + COLUMN_AMOUNT + " integer);";


    public RecentlyAddedDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RecentlyAddedDB_SQL.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENTLY);
        onCreate(db);
    }
}
