package pl.grudowska.feedme.utils;


import android.content.Context;
import android.widget.Toast;

public enum StatusCode {
    SUCCESS, FILE_NOT_FOUND, SERVER_DOWN, FAIL, OTHER;

    public static void showStatus(Context context, StatusCode status) {
        switch (status) {
            case SUCCESS: {
                Toast.makeText(context, "Database has been successfully updated", Toast.LENGTH_SHORT).show();
                break;
            }
            case FILE_NOT_FOUND: {
                Toast.makeText(context, "Missing file on the server", Toast.LENGTH_SHORT).show();
                break;
            }
            case SERVER_DOWN: {
                Toast.makeText(context, "Server is down", Toast.LENGTH_SHORT).show();
                break;
            }
            case FAIL: {
                Toast.makeText(context, "While update an error occurred", Toast.LENGTH_SHORT).show();
                break;
            }
            case OTHER: {
                Toast.makeText(context, "Connection problem", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
