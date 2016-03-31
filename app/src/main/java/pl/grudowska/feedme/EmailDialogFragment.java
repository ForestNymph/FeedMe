package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pl.grudowska.feedme.util.SharedPreferencesManager;

public class EmailDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.email_dialog, null);
        TextView currentEmailView = (TextView) dialogView.findViewById(R.id.email_current);
        builder.setView(dialogView)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Dialog dialog = (Dialog) dialogInterface;
                        String mail_value = ((EditText) dialog.findViewById(R.id.useremail)).getText().toString();
                        if (mail_value.isEmpty()) {
                            // do nothing
                        } else {
                            SharedPreferencesManager.saveDataString(getActivity(), "email", mail_value);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        EmailDialogFragment.this.getDialog().cancel();
                    }
                });
        currentEmailView.setText(SharedPreferencesManager.loadDataString(getActivity(), "email", "test@test.pl"));
        return builder.create();
    }
}
