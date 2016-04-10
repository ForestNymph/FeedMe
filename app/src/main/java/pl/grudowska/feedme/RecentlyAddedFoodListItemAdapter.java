package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.database.RecentlyAdded;
import pl.grudowska.feedme.database.RecentlyAddedDataSource;

public class RecentlyAddedFoodListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<RecentlyAdded> mValues;

    RecentlyAddedFoodListItemAdapter(final Context context) {
        mContext = context;

        // Open DB's
        RecentlyAddedDataSource dataSource = new RecentlyAddedDataSource(mContext);
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
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.activity_card_summary_left_textview);
            viewHolder.textView_amount = (TextView) convertView.findViewById(R.id.activity_card_summary_right_imageview);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView_name.setText(mValues.get(getItem(position)).getProduct());
        String amount = mValues.get(getItem(position)).getAmount() + " gramm";
        viewHolder.textView_amount.setText(amount);

        return convertView;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_amount;
    }
}
