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

        RecentlyAddedDataSource dataSource = new RecentlyAddedDataSource(mContext);
        dataSource.open();

        mValues = dataSource.getAllAddedProducts();

        for (int i = 0; i < mValues.size(); ++i) {
            add(i);
        }
        int j = 0;
        while (mValues.size() < 10) {
            mValues.add(j++, dataSource.createProduct("Product ", j));
        }

        dataSource.close();
    }

    //@Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.content_recentlyadded_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) view.findViewById(R.id.activity_card_summary_left_textview);
            viewHolder.textView_amount = (TextView) view.findViewById(R.id.activity_card_summary_right_imageview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        // viewHolder.textView_name.setText(mFoodType.get(getItem(position)));
        // viewHolder.textView_amount.setText(mContext.getString(R.string.card_number, getItem(position) + 1));

        viewHolder.textView_name.setText(mValues.get(position).getProduct());
        String amount = mValues.get(position).getAmount() + " gramm";
        viewHolder.textView_amount.setText(amount);

        return view;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_amount;
    }
}
