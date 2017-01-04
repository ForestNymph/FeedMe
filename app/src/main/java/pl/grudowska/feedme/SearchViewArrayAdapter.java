package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;

public class SearchViewArrayAdapter extends ArrayAdapter<Product> {

    private final Context mContext;
    private List<Product> mValues;

    SearchViewArrayAdapter(final Context context, List<Product> values) {
        mContext = context;
        mValues = values;
    }

    public void updateProductsList(List<Product> products) {
        mValues = products;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_search_list_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.search_name_tv);

            final ViewSwitcher switcher = (ViewSwitcher) convertView.findViewById(R.id.amount_search_swchr);
            switcher.getChildAt(0);

            viewHolder.textView_amount = (TextView) switcher.findViewById(R.id.search_amount_tv);
            viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_search_amount_ev);

            viewHolder.edit_btn = (Button) convertView.findViewById(R.id.card_search_edit_btn);
            viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double amount = getNewProductAmount(switcher);
                    if (amount != -1) {
                        mValues.get(position).amount = amount;
                        notifyDataSetChanged();
                    }
                }
            });
            viewHolder.add_btn = (Button) convertView.findViewById(R.id.card_search_add_btn);
            viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product prod = mValues.get(position);
                    if (prod.amount != 0) {
                        AddedProductDataSource dataSource = new AddedProductDataSource(mContext, false);
                        dataSource.open();
                        dataSource.createSimpleAddedProduct(prod);
                        dataSource.close();
                        // reset amount value
                        mValues.get(position).amount = 0;
                        Toast.makeText(mContext, mValues.get(position).name + " added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "No amount added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product prod = mValues.get(position);
        viewHolder.textView_name.setText(prod.name);
        String amount = prod.amount + " g";
        viewHolder.textView_amount.setText(amount);

        // set focus for all edit fields in list
        viewHolder.editView_newamount.requestFocus();

        return convertView;
    }

    //http://stackoverflow.com/questions/15194835/filtering-custom-adapter-indexoutofboundsexception
    @Override
    public int getCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    @Override
    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }

    private double getNewProductAmount(ViewSwitcher switcher) {

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_search_amount_ev);
        String newAmount;

        switcher.showNext();

        // if active view is edit view get new amount of product
        if (switcher.getCurrentView().onCheckIsTextEditor()) {
            viewHolder.editView_newamount.setEnabled(true);
            viewHolder.editView_newamount.setFocusable(true);
            viewHolder.editView_newamount.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(viewHolder.editView_newamount, InputMethodManager.SHOW_IMPLICIT);
        } else {
            newAmount = viewHolder.editView_newamount.getText().toString();
            if (newAmount.isEmpty()) {
                return -1;
            } else {
                viewHolder.editView_newamount.getText().clear();
                return Double.valueOf(newAmount);
            }
        }
        return -1;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_amount;
        EditText editView_newamount;
        Button edit_btn;
        Button add_btn;
    }
}
