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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class DetailsDialogFragment extends DialogFragment {

    private AlertDialog mDialog;

    @Override
    @NonNull
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

        List<Details> mDetails = createDetailsList(getTag());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
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
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
            }
        });
        return mDialog;
    }

    private List<Details> createDetailsList(String id) {
        ProductDataSource dataSource = new ProductDataSource(getActivity());
        List<Details> details = new ArrayList<>();
        try {
            dataSource.openDataBase();
        } catch (ProductDataSource.DatabaseNotExistException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "The database must be updated", Toast.LENGTH_LONG).show();
        }
        Product product = dataSource.getProduct(Long.parseLong(id));
        dataSource.close();

        details.add(new Details("Energy: ", product.kcal + " kcal"));
        details.add(new Details("Protein: ", product.protein + " g"));
        details.add(new Details("Carbohydrates: ", product.carbohydrates + " g"));
        details.add(new Details("Fiber: ", product.fiber + " g"));
        details.add(new Details("Fats: ", product.fats + " g"));
        if (product.fatsSaturated == -1) {
            details.add(new Details("Saturated: ", "None"));
            details.add(new Details("Monosaturated: ", "None"));
            details.add(new Details("Omega3: ", "None"));
            details.add(new Details("Omega6: ", "None"));
        } else {
            details.add(new Details("Saturated: ", product.fatsSaturated + " g"));
            details.add(new Details("Monosaturated: ", product.fatsMonounsaturated + " g"));
            details.add(new Details("Omega3: ", product.omega3 + " g"));
            details.add(new Details("Omega6: ", product.omega6 + " g"));
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
