package pl.grudowska.feedme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.List;

import pl.grudowska.feedme.databases.RecentlyAddedDataSource;
import pl.grudowska.feedme.databases.SpecificFood;
import pl.grudowska.feedme.databases.SpecificFoodDataSource;

public class SpecificFoodTypeListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;

    private List<SpecificFood> mFoodType;

    public SpecificFoodTypeListItemAdapter(final Context context, String type) {
        super(context, R.layout.content_specific_card, R.id.card_title, R.id.card_content);

        mContext = context;

        SpecificFoodDataSource dataSource = new SpecificFoodDataSource(mContext);
        dataSource.open();
        mFoodType = dataSource.getProductsByType(type);
        dataSource.close();

        for (int i = 0; i < mFoodType.size(); ++i) {
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
        tv.setText(mFoodType.get(getItem(position)).getName());
        tv.setTextSize(20);
        return tv;
    }

    @NonNull
    @Override
    public View getContentView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_specific_card, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.buttonView_1 = (Button) convertView.findViewById(R.id.button_default_one);
            viewHolder.buttonView_2 = (Button) convertView.findViewById(R.id.button_default_two);
            viewHolder.buttonView_3 = (Button) convertView.findViewById(R.id.button_default_three);
            viewHolder.buttonView_add = (Button) convertView.findViewById(R.id.button_add_custom_amount);
            viewHolder.editView = (EditText) convertView.findViewById(R.id.edittext_custom_amount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setListeners(viewHolder, position);

        viewHolder.buttonView_1.setText(mFoodType.get(getItem(position)).getDef1() + "");
        viewHolder.buttonView_2.setText(mFoodType.get(getItem(position)).getDef2() + "");
        viewHolder.buttonView_3.setText(mFoodType.get(getItem(position)).getDef3() + "");

        return convertView;
    }

    private void setListeners(final ViewHolder viewHolder, final int position) {

        viewHolder.buttonView_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mFoodType.get(getItem(position)).getDef1());
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mFoodType.get(getItem(position)).getDef2());
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mFoodType.get(getItem(position)).getDef3());
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int amount;
                String resultEditText = viewHolder.editView.getText().toString();
                try {
                    amount = Integer.parseInt(resultEditText);
                    addProductToDB(position, amount);
                    afterAddedProductAction(position);
                    viewHolder.editView.getText().clear();
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
        });
    }

    private void addProductToDB(int position, int amount) {
        RecentlyAddedDataSource mDataSource = new RecentlyAddedDataSource(mContext);
        mDataSource.open();
        mDataSource.createProduct(mFoodType.get(getItem(position)).getName(), amount);
        mDataSource.close();
    }

    private void afterAddedProductAction(int position) {
        collapse(position);
        Toast.makeText(mContext, mFoodType.get(getItem(position)) + " added", Toast.LENGTH_SHORT).show();
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
