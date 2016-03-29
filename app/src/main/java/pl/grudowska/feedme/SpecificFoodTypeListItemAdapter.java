package pl.grudowska.feedme;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class SpecificFoodTypeListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;

    // dummy data
    ArrayList<String> mFoodName = new ArrayList<>(Arrays.asList(
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

    public SpecificFoodTypeListItemAdapter(final Context context) {
        super(context, R.layout.activity_specific_food_item_card, R.id.card_title, R.id.card_content);
        mContext = context;

        for (int i = 0; i < mFoodName.size(); ++i) {
            add(i);
        }
    }

    @NonNull
    @Override
    public View getTitleView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        TextView tv = (TextView) convertView;

        if (tv == null) {
            tv = new TextView(mContext);
        }
        tv.setText(mFoodName.get(position));
        tv.setTextSize(20);
        return tv;
    }

    @NonNull
    @Override
    public View getContentView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_specific_food_item_card, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.buttonView_1 = (Button) view.findViewById(R.id.button_default_one);
            viewHolder.buttonView_2 = (Button) view.findViewById(R.id.button_default_two);
            viewHolder.buttonView_3 = (Button) view.findViewById(R.id.button_default_three);
            viewHolder.buttonView_add = (Button) view.findViewById(R.id.button_add_custom_amount);
            viewHolder.editView = (EditText) view.findViewById(R.id.edittext_custom_amount);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setListeners(viewHolder);
        return view;
    }

    private void setListeners(final ViewHolder viewHolder) {
        viewHolder.buttonView_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Button 1", "click");
            }
        });
        viewHolder.buttonView_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Button 2", "click");
            }
        });
        viewHolder.buttonView_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Button 3", "click");
            }
        });
        viewHolder.buttonView_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = viewHolder.editView.getText().toString();
                Log.d("Edit text", value);
                if (value.isEmpty()) {
                    // do nothing
                }
                // action
                // 1. add to eaten product list
                // 2. close product fiche
                // 3. Toast about added amount to list
            }
        });
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        Button buttonView_1;
        Button buttonView_2;
        Button buttonView_3;
        Button buttonView_add;
        EditText editView;
    }
}
