package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pl.grudowska.feedme.alghoritms.CalculateSummary;

public class SummaryDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        List<SummaryResult> results = CalculateSummary.calculate(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View summaryDialogView = inflater.inflate(R.layout.summary_dialog_listview, null);
        ListView summaryListView;

        if (results != null) {
            summaryListView = (ListView) summaryDialogView.findViewById(R.id.list_summmary);
            summaryListView.setAdapter(new SummaryDialogAdapter(getActivity(), results));
        }
        builder.setView(summaryDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummaryDialogFragment.this.getDialog().cancel();
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

    private class SummaryDialogAdapter extends ArrayAdapter<SummaryResult> {

        private Context mContext;
        private List<SummaryResult> mResults;

        public SummaryDialogAdapter(Context context, List<SummaryResult> result) {
            super(context, -1, result);

            mContext = context;
            mResults = result;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.summary_dialog_card, parent, false);
            TextView product_textView = (TextView) rowView.findViewById(R.id.summary_product);
            TextView amount_textView = (TextView) rowView.findViewById(R.id.summary_amount);

            product_textView.setText(mResults.get(position).getResultType());
            String amount = mResults.get(position).getAmount() + mResults.get(position).getUnit();
            amount_textView.setText(amount);

            return rowView;
        }
    }
}

