package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AboutDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String verName = BuildConfig.VERSION_NAME;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View aboutDialogView = inflater.inflate(R.layout.about_dialog, null);
        builder.setView(aboutDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        TextView aboutTv = (TextView) aboutDialogView.findViewById(R.id.about_view);
        aboutTv.append(verName);

        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(),
                                R.color.colorTextGray));
            }
        });
        return mDialog;
    }
}
