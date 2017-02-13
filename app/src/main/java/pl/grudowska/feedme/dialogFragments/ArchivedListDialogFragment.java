package pl.grudowska.feedme.dialogFragments;

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

import java.util.List;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.EmailManager;

public class ArchivedListDialogFragment extends DialogFragment {

    private AlertDialog mDialog;
    private String mDate;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = getTag();

        final List<Product> productList = DatabaseManager.getArchivedProductsByDateDB(getActivity(), mDate);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View archivedDialogView = inflater.inflate(R.layout.archived_dialog, null);

        TextView titleDate = (TextView) archivedDialogView.findViewById(R.id.archived_current_date_tv);
        String date = "[" + mDate + "]";
        titleDate.setText(date);

        ListView archivedListView = (ListView) archivedDialogView.findViewById(R.id.list_archived);
        archivedListView.setAdapter(new ArchivedListAdapter(getActivity(), productList));

        builder.setView(archivedDialogView)
                .setPositiveButton(R.string.reuse, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AddedProductDataSource dataSource = new AddedProductDataSource(getActivity());
                        dataSource.open();
                        for (int i = 0; i < productList.size(); ++i) {
                            dataSource.createSimpleAddedProduct(productList.get(i));
                        }
                        dataSource.close();

                        Toast.makeText(getActivity(), R.string.reuse_list, Toast.LENGTH_SHORT).show();
                        getDialog().cancel();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getDialog().cancel();
            }
        }).setNeutralButton(R.string.resend, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String contentMail = DatabaseManager.getContentMailByDateDB(getActivity(), mDate);
                new EmailManager(getActivity(), mDate, contentMail);
                Toast.makeText(getActivity(), R.string.sent_message, Toast.LENGTH_SHORT).show();
                getDialog().cancel();
            }
        });
        mDialog = builder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                        .setTextColor(ContextCompat.getColor(mDialog.getContext(), R.color.colorTextGray));
            }
        });
        return mDialog;
    }

    private class ArchivedListAdapter extends ArrayAdapter<Product> {

        final private Context mContext;
        final private List<Product> mProducts;

        ArchivedListAdapter(Context context, List<Product> products) {
            super(context, -1, products);
            mContext = context;
            mProducts = products;
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.content_archived_product_row, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.product_textView = (TextView) convertView.findViewById(R.id.archived_product_tv);
                viewHolder.amount_textView = (TextView) convertView.findViewById(R.id.archived_amount_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Product archived = mProducts.get(position);

            String name = archived.name;
            String amount = archived.amount + " g";
            viewHolder.product_textView.setText(name);
            viewHolder.amount_textView.setText(amount);

            return convertView;
        }

        private class ViewHolder {
            TextView product_textView;
            TextView amount_textView;
        }
    }
}

