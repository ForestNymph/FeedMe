package pl.grudowska.feedme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.ArrayList;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.dialogFragments.DetailsDialogFragment;


public class SpecificFoodTypeArrayAdapter extends ExpandableListItemAdapter<Product> {

    private final Context mContext;

    public SpecificFoodTypeArrayAdapter(final Context context, ArrayList<Product> products) {
        super(context, R.layout.content_specific_row, R.id.card_title, R.id.card_content, products);
        mContext = context;
    }

    @NonNull
    @Override
    public View getTitleView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = new TextView(mContext);
        }
        tv.setText(getItem(position).name);
        tv.setTextSize(20);
        return tv;
    }

    @NonNull
    @Override
    public View getContentView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_specific_row, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.buttonView_1 = (Button) convertView.findViewById(R.id.button_default_one);
            viewHolder.buttonView_2 = (Button) convertView.findViewById(R.id.button_default_two);
            viewHolder.buttonView_3 = (Button) convertView.findViewById(R.id.button_default_three);
            viewHolder.buttonView_add = (Button) convertView.findViewById(R.id.button_add_custom_amount);
            viewHolder.editView = (EditText) convertView.findViewById(R.id.edittext_custom_amount);
            viewHolder.buttonDetails = (Button) convertView.findViewById(R.id.button_details);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setListeners(viewHolder, position);

        viewHolder.buttonView_1.setText(String.valueOf(getItem(position).def1));
        viewHolder.buttonView_2.setText(String.valueOf(getItem(position).def2));
        viewHolder.buttonView_3.setText(String.valueOf(getItem(position).def3));

        return convertView;
    }

    private void setListeners(final ViewHolder viewHolder, final int position) {

        viewHolder.buttonView_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, getItem(position).def1);
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, getItem(position).def2);
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, getItem(position).def3);
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
        viewHolder.buttonDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DetailsDialogFragment details = new DetailsDialogFragment();
                FragmentActivity activity = (FragmentActivity) mContext;
                FragmentManager fm = activity.getSupportFragmentManager();
                details.show(fm, String.valueOf(getItem(position).id));
            }
        });
    }

    private void addProductToDB(int position, int amount) {
        ProductDataSource dataSourceProduct = new ProductDataSource(mContext);
        AddedProductDataSource dataSourceAdded = new AddedProductDataSource(mContext);
        try {
            dataSourceProduct.openDataBase();
        } catch (ProductDataSource.DatabaseNotExistException e) {
            e.printStackTrace();
        }
        dataSourceAdded.open();
        Product product = dataSourceProduct.getProduct(getItem(position).id);
        product.amount = amount;
        dataSourceAdded.createSimpleAddedProduct(product);
        dataSourceProduct.close();
        dataSourceAdded.close();
    }

    private void afterAddedProductAction(int position) {
        collapse(position);
        CalculateSummary.warningIfCalorieLimitExceeded(mContext);
        Toast.makeText(mContext, getItem(position).name + " added", Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        Button buttonView_1;
        Button buttonView_2;
        Button buttonView_3;
        Button buttonView_add;
        EditText editView;
        Button buttonDetails;
    }
}
