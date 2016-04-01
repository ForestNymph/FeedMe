package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pl.grudowska.feedme.util.EmailManager;

public class AboutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        new EmailManager(getActivity());

        String verName = BuildConfig.VERSION_NAME;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View aboutDialogView = inflater.inflate(R.layout.about_dialog, null);
        builder.setView(aboutDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AboutDialogFragment.this.getDialog().cancel();
                    }
                });
        TextView aboutTv = (TextView) aboutDialogView.findViewById(R.id.about_view);
        aboutTv.append(verName);

        return builder.create();
    }
}
