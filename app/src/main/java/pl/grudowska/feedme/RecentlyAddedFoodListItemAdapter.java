package pl.grudowska.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class RecentlyAddedFoodListItemAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    // dummy data
    private ArrayList<String> mFoodType = new ArrayList<>(Arrays.asList(
            "Product 1", "Product 2", "Product 3",
            "Product 4", "Product 5", "Product 6",
            "Product 7", "Product 8", "Product 9",
            "Product 10", "Product 11", "Product 12",
            "Product 13", "Product 14", "Product 15",
            "Product 16", "Product 17", "Product 18",
            "Product 19", "Product 20", "Product 21",
            "Product 22", "Product 23", "Product 24",
            "Product 25", "Product 26", "Product 27",
            "Product 28", "Product 29", "Product 30"));

    RecentlyAddedFoodListItemAdapter(final Context context) {
        mContext = context;
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
        viewHolder.textView_name.setText(mFoodType.get(getItem(position)));
        viewHolder.textView_amount.setText(mContext.getString(R.string.card_number, getItem(position) + 1));

        return view;
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_amount;
    }
}
