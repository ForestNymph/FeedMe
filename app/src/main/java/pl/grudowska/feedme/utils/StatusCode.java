package pl.grudowska.feedme.utils;


import android.content.Context;
import android.widget.Toast;

enum StatusCode {
    SUCCESS, SYNC_SUCCESS, FILE_NOT_FOUND, SEND_DATABASE, SERVER_DOWN, FAIL, OTHER;

    public static void showStatus(Context context, StatusCode status) {
        switch (status) {
            case SUCCESS: {
                Toast.makeText(context, "Database has been successfully updated", Toast.LENGTH_SHORT).show();
                break;
            }
            case SYNC_SUCCESS: {
                Toast.makeText(context, "Database has been successfully synced", Toast.LENGTH_SHORT).show();
                break;
            }
            case FILE_NOT_FOUND: {
                Toast.makeText(context, "Missing file on the server", Toast.LENGTH_SHORT).show();
                break;
            }
            case SEND_DATABASE: {
                Toast.makeText(context, "Missing database on the server, database was sent", Toast.LENGTH_SHORT).show();
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
