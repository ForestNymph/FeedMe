package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class EmailDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.email_dialog, null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Dialog dialog = (Dialog) dialogInterface;
                        String mailTo_value = ((EditText) dialog.findViewById(R.id.mailto_mail)).getText().toString();
                        if (mailTo_value.isEmpty()) {
                            // do nothing
                        } else {
                            SharedPreferencesManager.saveDataString(getActivity(), "mailTo", mailTo_value);
                        }
                        String mailFrom_value = ((EditText) dialog.findViewById(R.id.mailfrom_mail)).getText().toString();
                        if (mailFrom_value.isEmpty()) {
                            // do nothing
                        } else {
                            SharedPreferencesManager.saveDataString(getActivity(), "mailFrom", mailFrom_value);
                        }
                        String password_value = ((EditText) dialog.findViewById(R.id.mailfrom_password)).getText().toString();
                        if (password_value.isEmpty()) {
                            // do nothing
                        } else {
                            SharedPreferencesManager.saveDataString(getActivity(), "password", password_value);
                        }
                        Toast.makeText(getActivity(), "Email settings updated", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        EmailDialogFragment.this.getDialog().cancel();
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
}
