package pl.grudowska.feedme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class AllSentFoodListItemAdapter extends ExpandableListItemAdapter<Integer> {


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

    public AllSentFoodListItemAdapter(final Context context) {
        super(context, R.layout.content_showall_card, R.id.card_title, R.id.card_content);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.content_showall_card, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.textView_fulldaylist = (TextView) view.findViewById(R.id.showll_item);
            viewHolder.buttonView_resend = (Button) view.findViewById(R.id.button_resend);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setListeners(viewHolder);
        return view;
    }

    private void setListeners(final ViewHolder viewHolder) {
        viewHolder.buttonView_resend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Resend", "click");

                Toast.makeText(mContext, "Message was sent", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView_fulldaylist;
        Button buttonView_resend;
    }
}
