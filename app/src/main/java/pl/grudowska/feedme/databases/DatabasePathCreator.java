package pl.grudowska.feedme.databases;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DatabasePathCreator extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";

    public DatabasePathCreator(Context base) {
        super(base);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public File getDatabasePath(String name) {
        File sdcard = Environment.getExternalStorageDirectory();
        String dbfile = sdcard.getAbsolutePath() + File.separator + "databases" + File.separator + name;

        if (!dbfile.endsWith(".db")) {
            dbfile += ".db";
        }
        File result = new File(dbfile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }
        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
            Log.w(DEBUG_CONTEXT,
                    "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
        }
        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        if (isExternalStorageWritable()) {
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
            if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
                Log.w(DEBUG_CONTEXT, "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
            }
            return result;
        }
        return null;
    }
}
