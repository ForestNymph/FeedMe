package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class RecentlyAddedFoodListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<Product> mValues;

    RecentlyAddedFoodListItemAdapter(final Context context) {
        mContext = context;

        // Open DB's
        ProductDataSource dataSource = new ProductDataSource(mContext);
        dataSource.open();

        mValues = dataSource.getAllAddedProducts();

        for (int i = 0; i < mValues.size(); ++i) {
            add(i);
        }
        // Close DB's
        dataSource.close();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_recentlyadded_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.card_recently_name_textview);
            viewHolder.textView_amount = (TextView) convertView.findViewById(R.id.card_recently_amount_textview);
            viewHolder.textView_kcal = (TextView) convertView.findViewById(R.id.card_recently_kcal_textview);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product prod = mValues.get(getItem(position));
        viewHolder.textView_name.setText(prod.getName());
        String kcal = prod.getKcalRelatedWithAmount() + " kcal";
        viewHolder.textView_kcal.setText(kcal);
        String amount = prod.getAmount() + " g";
        viewHolder.textView_amount.setText(amount);

        return convertView;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_kcal;
        TextView textView_amount;
    }
}
