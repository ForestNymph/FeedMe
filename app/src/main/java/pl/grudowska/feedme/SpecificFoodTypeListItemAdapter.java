package pl.grudowska.feedme;

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

import java.util.List;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataBase;
import pl.grudowska.feedme.databases.SupplementaryInfoDataSource;
import pl.grudowska.feedme.utils.SharedPreferencesManager;


public class SpecificFoodTypeListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;

    private List<Product> mProduct;

    public SpecificFoodTypeListItemAdapter(final Context context, String typeName) {
        super(context, R.layout.content_specific_card, R.id.card_title, R.id.card_content);

        mContext = context;

        ProductDataBase dataSource = new ProductDataBase(mContext);
        try {
            dataSource.openDataBase();
            mProduct = dataSource.getProductsByType(typeName);
            dataSource.close();

            for (int i = 0; i < mProduct.size(); ++i) {
                add(i);
            }
        } catch (ProductDataBase.DatabaseNotExistException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "The database must be updated", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public View getTitleView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = new TextView(mContext);
        }
        tv.setText(mProduct.get(getItem(position)).getName());
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
            viewHolder.buttonDetails = (Button) convertView.findViewById(R.id.button_details);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setListeners(viewHolder, position);

        viewHolder.buttonView_1.setText(mProduct.get(getItem(position)).getDef1() + "");
        viewHolder.buttonView_2.setText(mProduct.get(getItem(position)).getDef2() + "");
        viewHolder.buttonView_3.setText(mProduct.get(getItem(position)).getDef3() + "");

        return convertView;
    }

    private void setListeners(final ViewHolder viewHolder, final int position) {

        viewHolder.buttonView_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mProduct.get(getItem(position)).getDef1());
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mProduct.get(getItem(position)).getDef2());
                afterAddedProductAction(position);
            }
        });
        viewHolder.buttonView_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addProductToDB(position, mProduct.get(getItem(position)).getDef3());
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
                FragmentActivity activity = (FragmentActivity) (mContext);
                FragmentManager fm = activity.getSupportFragmentManager();
                details.show(fm, mProduct.get(getItem(position)).getId() + "");
            }
        });
    }

    private void addProductToDB(int position, int amount) {
        ProductDataBase dataSourceProduct = new ProductDataBase(mContext);
        SupplementaryInfoDataSource dataSourceDescription = new SupplementaryInfoDataSource(mContext);
        try {
            dataSourceProduct.openDataBase();
        } catch (ProductDataBase.DatabaseNotExistException e) {
            e.printStackTrace();
        }
        dataSourceDescription.open();
        Product product = dataSourceProduct.getProduct(mProduct.get(getItem(position)).getId());
        product.setAmount(amount);
        dataSourceDescription.createSimpleAddedProduct(product);
        dataSourceProduct.close();
        dataSourceDescription.close();
    }

    private void afterAddedProductAction(int position) {
        collapse(position);
        ifCalorieLimitExceeded();
        Toast.makeText(mContext, mProduct.get(getItem(position)).getName() + " added", Toast.LENGTH_SHORT).show();
    }

    private void ifCalorieLimitExceeded() {
        if (CalculateSummary.getTotalKcal(mContext) > SharedPreferencesManager.loadDataInt(mContext, "limit", 2300)) {
            FragmentActivity activity = (FragmentActivity) (mContext);
            FragmentManager fm = activity.getSupportFragmentManager();
            WarningLimitDialogFragment dialog = new WarningLimitDialogFragment();
            dialog.show(fm, "");
        }
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        Button buttonView_1;
        Button buttonView_2;
        Button buttonView_3;
        Button buttonView_add;
        EditText editView;
        Button buttonDetails;
    }
}
