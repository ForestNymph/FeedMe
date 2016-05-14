package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.DatabaseManager;

public class RecentlyAddedFoodArrayAdapter extends ArrayAdapter<Product> {

    private final Context mContext;
    private List<Product> mValues;

    RecentlyAddedFoodArrayAdapter(final Context context) {
        mContext = context;
        dataLoader();
    }

    public void dataLoader() {
        mValues = DatabaseManager.getAddedProductsDB(mContext);
        for (int i = 0; i < mValues.size(); ++i) {
            add(mValues.get(i));
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_recentlyadded_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.recently_name_tv);
            viewHolder.textView_kcal = (TextView) convertView.findViewById(R.id.recently_kcal_tv);

            final ViewSwitcher switcher = (ViewSwitcher) convertView.findViewById(R.id.amount_swchr);
            switcher.getChildAt(0);

            viewHolder.textView_amount = (TextView) switcher.findViewById(R.id.recently_amount_tv);
            viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_amount_ev);

            viewHolder.edit_btn = (Button) convertView.findViewById(R.id.card_recently_edit_btn);
            viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double amount = getNewProductAmount(switcher);
                    if (amount != 0) {
                        Product prod = mValues.get(position);
                        AddedProductDataSource dataSource = new AddedProductDataSource(mContext);
                        dataSource.open();
                        dataSource.modifyAmountOfAddedProduct(prod, amount);
                        dataSource.close();

                        mValues.clear();
                        mValues = DatabaseManager.getAddedProductsDB(mContext);
                        notifyDataSetChanged();
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product prod = mValues.get(position);
        viewHolder.textView_name.setText(prod.name);
        String kcal = prod.getKcalRelatedWithAmount() + " kcal";
        viewHolder.textView_kcal.setText(kcal);
        String amount = prod.amount + " g";
        viewHolder.textView_amount.setText(amount);

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
        viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_amount_ev);
        String newAmount;

        switcher.showNext();

        // if active view is edit view get new amount of product
        if (switcher.getCurrentView().onCheckIsTextEditor()) {
            viewHolder.editView_newamount.setEnabled(true);
            viewHolder.editView_newamount.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(viewHolder.editView_newamount, InputMethodManager.SHOW_IMPLICIT);
        } else {
            newAmount = viewHolder.editView_newamount.getText().toString();
            if (newAmount.isEmpty()) {
                // do nothing
            } else {
                viewHolder.editView_newamount.getText().clear();
                return Double.valueOf(newAmount);
            }
        }
        return 0;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_kcal;
        TextView textView_amount;
        EditText editView_newamount;
        Button edit_btn;
    }
}
