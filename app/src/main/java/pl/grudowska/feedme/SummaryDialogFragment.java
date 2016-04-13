package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pl.grudowska.feedme.alghoritms.CalculateSummary;

public class SummaryDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View summaryDialogView = inflater.inflate(R.layout.summary_dialog, null);

        builder.setView(summaryDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummaryDialogFragment.this.getDialog().cancel();
                    }
                });
        TextView summary_product = (TextView) summaryDialogView.findViewById(R.id.summary_text_left);
        String summary = CalculateSummary.calculate(getActivity());
        summary_product.setText(summary);
        return builder.create();
    }
}
