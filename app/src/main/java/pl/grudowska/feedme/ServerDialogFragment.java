package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import pl.grudowska.feedme.utils.SharedPreferencesManager;
import pl.grudowska.feedme.utils.StatusCode;

public class ServerDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.server_dialog, null);

        // set previous or default configuration in editview
        ((EditText) dialogView.findViewById(R.id.server_address))
                .setText(ProductDataSource.getDatabaseAdress(getActivity()));
        ((EditText) dialogView.findViewById(R.id.port_address))
                .setText(String.valueOf(ProductDataSource.getPortNumber(getActivity())));
        ((EditText) dialogView.findViewById(R.id.db_name))
                .setText(ProductDataSource.getDatabaseName(getActivity()));

        Button sync = (Button) dialogView.findViewById(R.id.db_sync_btn);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigurationData(mDialog);
                // Update products database
                new DownloadMainDatabaseTask().execute(ProductDataSource.getDatabaseAdress(getActivity())
                        + ProductDataSource.getDatabaseName(getActivity()));
            }
        });

        builder.setView(dialogView)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        saveConfigurationData(dialogInterface);
                        Toast.makeText(getActivity(), "Database configuration updated", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        ServerDialogFragment.this.getDialog().cancel();
                    }
                });
        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
            }
        });

        return mDialog;
    }

    private void saveConfigurationData(DialogInterface dialogInterface) {
        Dialog dialog = (Dialog) dialogInterface;
        String server = ((EditText) dialog.findViewById(R.id.server_address)).getText().toString();
        if (server.isEmpty()) {
            // do nothing
        } else {
            SharedPreferencesManager.saveDataString(getActivity(), "serverAddress", server);
        }
        String port = ((EditText) dialog.findViewById(R.id.port_address)).getText().toString();
        int portNr = 0;
        if (port.isEmpty()) {
            // do nothing
        } else {
            try {
                portNr = Integer.valueOf(port);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                portNr = 8090;
            }
        }
        SharedPreferencesManager.saveDataInt(getActivity(), "serverPort", portNr);
        String database = ((EditText) dialog.findViewById(R.id.db_name)).getText().toString();
        if (port.isEmpty()) {
            // do nothing
        } else {
            SharedPreferencesManager.saveDataString(getActivity(), "databaseName", database);
        }
    }

    private class DownloadMainDatabaseTask extends AsyncTask<String, Void, StatusCode> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Connecting to the server...wait", Toast.LENGTH_LONG).show();
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

                String databasePath = getActivity()
                        .getDatabasePath(ProductDataSource.getDatabaseName(getActivity())).toString();

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

            StatusCode.showStatus(getActivity(), status);
        }
    }
}
