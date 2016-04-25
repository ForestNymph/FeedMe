package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.DatabaseManager;

public class RecentlyAddedFoodListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<Product> mValues;

    RecentlyAddedFoodListItemAdapter(final Context context) {
        mContext = context;

        mValues = DatabaseManager.getAddedProductsDB(mContext);

        for (int i = 0; i < mValues.size(); ++i) {
            add(i);
        }
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
        viewHolder.textView_name.setText(prod.name);
        String kcal = prod.getKcalRelatedWithAmount() + " kcal";
        viewHolder.textView_kcal.setText(kcal);
        String amount = prod.amount + " g";
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
