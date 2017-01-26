package pl.grudowska.feedme.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import pl.grudowska.feedme.databases.ProductDataSource;

public class DownloadDatabaseTask extends AsyncTask<String, Void, StatusCode> {

    final private Context mContext;

    public DownloadDatabaseTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mContext, "Connecting to the dialog_server...wait", Toast.LENGTH_LONG).show();
    }

    @Override
    protected StatusCode doInBackground(String... urls) {
        URL url = null;
        try {
            url = new URL(urls[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection;
        InputStream input;
        try {
            assert url != null;
            connection = url.openConnection();
            connection.connect();

            input = new BufferedInputStream(url.openStream());

            String databasePath = mContext
                    .getDatabasePath(ProductDataSource.getDatabaseName(mContext)).toString();

            OutputStream output = new FileOutputStream(databasePath);
            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

        } catch (ConnectException e) {
            e.printStackTrace();
            return StatusCode.SERVER_DOWN;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return StatusCode.FILE_NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusCode.FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            return StatusCode.OTHER;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected void onPostExecute(StatusCode status) {
        super.onPostExecute(status);

        StatusCode.showStatus(mContext, status);
    }
}

