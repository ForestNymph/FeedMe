package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class WarningLimitDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.warning_dialog, null);

        builder.setView(view);

        int limit = CalculateSummary.getTotalKcal(getActivity()) -
                SharedPreferencesManager.loadDataInt(getActivity(), "limit", 2300);

        TextView limitCurrent = (TextView) view.findViewById(R.id.exceeded_by);
        limitCurrent.setText(limit + "");

        builder.setPositiveButton(R.string.consequences,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        // do nothing
                        WarningLimitDialogFragment.this.getDialog().cancel();
                    }
                });

        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
            }
        });
        return mDialog;
    }
}
