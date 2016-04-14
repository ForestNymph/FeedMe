package pl.grudowska.feedme.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AllSentFoodDB_SQL extends SQLiteOpenHelper {

    public static final String TABLE_SENT = "sent_email";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "sent_date";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_KCAL = "kcal";

    private static final String DATABASE_NAME = "sentemail.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SENT + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " text not null, " + COLUMN_CONTENT + " text not null, " + COLUMN_KCAL + " integer);";


    public AllSentFoodDB_SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AllSentFoodDB_SQL.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENT);
        onCreate(db);
    }
}
