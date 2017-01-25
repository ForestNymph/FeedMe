package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;

public class ExpandProductDetailsDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    @NonNull
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

        AddedProductDataSource dataSource = new AddedProductDataSource(getActivity());
        dataSource.open();
        Product product = dataSource.getProduct(Long.parseLong(getTag()));
        dataSource.close();

        List<Details> mDetails = createDetailsList(product);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View details = inflater.inflate(R.layout.details_dialog_listview, null);

        TextView title_tv = (TextView) details.findViewById(R.id.product_name_tv);
        String product_title = product.name.toUpperCase();
        title_tv.setText(product_title);

        TextView contains_tv = (TextView) details.findViewById(R.id.details_tv);
        String contains = "Contains per " + product.amount + " g of product:";
        contains_tv.setText(contains);

        ListView details_lv = (ListView) details.findViewById(R.id.list_details);
        details_lv.setAdapter(new DetailsDialogAdapter(getActivity(), mDetails));

        builder.setView(details).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
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

    private List<Details> createDetailsList(Product product) {

        // Originally entered ingredients for product per 100 grams
        // Calculation of the product composition for the given amount value
        double factor = product.amount / 100;

        List<Details> details = new ArrayList<>();
        details.add(new Details("Energy: ",
                String.format(Locale.getDefault(), "%.1f", product.kcal * factor) + " kcal"));
        details.add(new Details("Protein: ",
                String.format(Locale.getDefault(), "%.1f", product.protein * factor) + " g"));
        details.add(new Details("Carbohydrates: ",
                String.format(Locale.getDefault(), "%.1f", product.carbohydrates * factor) + " g"));
        details.add(new Details("Fiber: ",
                String.format(Locale.getDefault(), "%.1f", product.fiber * factor) + " g"));
        details.add(new Details("Fats: ",
                String.format(Locale.getDefault(), "%.1f", product.fats * factor) + " g"));
        if (product.fatsSaturated == -1) {
            details.add(new Details("Saturated: ", "None"));
            details.add(new Details("Monosaturated: ", "None"));
            details.add(new Details("Omega3: ", "None"));
            details.add(new Details("Omega6: ", "None"));
        } else {
            details.add(new Details("Saturated: ",
                    String.format(Locale.getDefault(), "%.1f", product.fatsSaturated * factor) + " g"));
            details.add(new Details("Monosaturated: ",
                    String.format(Locale.getDefault(), "%.1f", product.fatsMonounsaturated * factor) + " g"));
            details.add(new Details("Omega3: ",
                    String.format(Locale.getDefault(), "%.1f", product.omega3 * factor) + " g"));
            details.add(new Details("Omega6: ",
                    String.format(Locale.getDefault(), "%.1f", product.omega6 * factor) + " g"));
        }
        return details;
    }

    private class Details {

        final public String name;
        final public String amount;

        Details(String name, String amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    private class DetailsDialogAdapter extends ArrayAdapter<Details> {
        final private Context mContext;
        final private List<Details> mDetails;

        DetailsDialogAdapter(Context context, List<Details> details) {
            super(context, -1, details);
            mContext = context;
            mDetails = details;
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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
            viewHolder.detail_textView.setText(mDetails.get(position).name);
            viewHolder.amount_textView.setText(mDetails.get(position).amount);
            return convertView;
        }

        private class ViewHolder {
            TextView detail_textView;
            TextView amount_textView;
        }
    }
}
