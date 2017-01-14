package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.SummaryRange;
import pl.grudowska.feedme.utils.DatabaseManager;

public class SummaryDialogFragment extends DialogFragment {

    private List<SummaryRange> mSummary;
    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        List<SummaryResult> results = CalculateSummary.calculate(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View summaryDialogView = inflater.inflate(R.layout.summary_dialog, null);

        mSummary = DatabaseManager.getAllSummariesDB(getActivity());

        ListView summaryListView;
        TextView summaryTextView;

        if (results == null) {
            summaryTextView = (TextView) summaryDialogView.findViewById(R.id.text_no_summmary);
            summaryTextView.setText(R.string.no_products);
        } else {
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
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
            }
        });
        return mDialog;
    }

    private class SummaryDialogAdapter extends ArrayAdapter<SummaryResult> {
        final private Context mContext;
        final private List<SummaryResult> mResults;

        SummaryDialogAdapter(Context context, List<SummaryResult> result) {
            super(context, -1, result);
            mContext = context;
            mResults = result;
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.summary_dialog_row, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.product_textView = (TextView) convertView.findViewById(R.id.summary_product);
                viewHolder.amount_textView = (TextView) convertView.findViewById(R.id.summary_amount);
                viewHolder.percentage_textView = (TextView) convertView.findViewById(R.id.summary_percentage);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            SummaryResult result = mResults.get(position);

            String name = "Total " + result.getResultType();
            String amount = result.getAmount() + result.getUnit();

            viewHolder.product_textView.setText(name);
            viewHolder.amount_textView.setText(amount);
            viewHolder.percentage_textView.setText("[0%]");

            if (!checkIfEnough(result.getSimpleName(), result.getAmount())) {
                viewHolder.amount_textView.setTextColor(Color.rgb(180, 0, 0));
                viewHolder.percentage_textView.setTextColor(Color.rgb(180, 0, 0));
                viewHolder.product_textView.setTextColor(Color.rgb(180, 0, 0));
            }
            return convertView;
        }

        private boolean checkIfEnough(String resName, int resAmount) {
            for (int i = 0; i < mSummary.size(); ++i) {
                if (mSummary.get(i).typeName.equals(resName)) {
                    return mSummary.get(i).minRange < resAmount;
                }
            }
            return true;
        }

        private class ViewHolder {
            TextView product_textView;
            TextView amount_textView;
            TextView percentage_textView;
        }
    }
}

