package pl.grudowska.feedme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class DetailsDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // TODO change getting data from activity
        List<Details> mDetails = createDetailsList(getTag());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View details = inflater.inflate(R.layout.details_dialog_listview, null);

        ListView details_listview = (ListView) details.findViewById(R.id.list_details);
        details_listview.setAdapter(new DetailsDialogAdapter(getActivity(), mDetails));

        builder.setView(details).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailsDialogFragment.this.getDialog().cancel();
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

    private List<Details> createDetailsList(String id) {
        ProductDataSource dataSource = new ProductDataSource(getActivity());
        dataSource.open();
        Product product = dataSource.getProduct(Long.parseLong(id));
        dataSource.close();

        List<Details> details = new ArrayList<>();
        details.add(new Details("Energy: ", product.getKcal() + " kcal"));
        details.add(new Details("Protein: ", product.getProtein() + " g"));
        details.add(new Details("Carbohydrates: ", product.getCarbohydrates() + " g"));
        details.add(new Details("Fiber: ", product.getFiber() + " g"));
        details.add(new Details("Fats: ", product.getFats() + " g"));
        if (product.getFatsSaturated() == -1) {
            details.add(new Details("Saturated: ", "None"));
            details.add(new Details("Monosaturated: ", "None"));
            details.add(new Details("Omega3: ", "None"));
            details.add(new Details("Omega6: ", "None"));
        } else {
            details.add(new Details("Saturated: ", product.getFatsSaturated() + " g"));
            details.add(new Details("Monosaturated: ", product.getFatsMonounsaturated() + " g"));
            details.add(new Details("Omega3: ", product.getOmega3() + " g"));
            details.add(new Details("Omega6: ", product.getOmega6() + " g"));
        }
        return details;
    }

    private class Details {

        private String mName;
        private String mAmount;

        public Details(String name, String amount) {
            mName = name;
            mAmount = amount;
        }

        public String getAmount() {
            return mAmount;
        }

        public void setAmount(String amount) {
            mAmount = amount;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }
    }

    private class DetailsDialogAdapter extends ArrayAdapter<Details> {
        private Context mContext;
        private List<Details> mDetails;

        public DetailsDialogAdapter(Context context, List<Details> details) {
            super(context, -1, details);
            mContext = context;
            mDetails = details;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.details_dialog_row, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.detail_textView = (TextView) convertView.findViewById(R.id.details_name_view);
                viewHolder.amount_textView = (TextView) convertView.findViewById(R.id.details_amount_view);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.detail_textView.setText(mDetails.get(position).getName());
            viewHolder.amount_textView.setText(mDetails.get(position).getAmount());
            return convertView;
        }

        private class ViewHolder {
            TextView detail_textView;
            TextView amount_textView;
        }
    }
}
