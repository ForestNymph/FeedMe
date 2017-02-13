package pl.grudowska.feedme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;

public class SearchViewArrayAdapter extends ArrayAdapter<Product> {

    private final Context mContext;
    private List<Product> mValues;

    public SearchViewArrayAdapter(final Context context, List<Product> values) {
        mContext = context;
        mValues = values;
    }

    public void updateProductsList(List<Product> products) {
        mValues = products;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row
        final ViewHolder viewHolder;
        final Product prod = mValues.get(position);

        // The adapters are built to reuse Views, when a View is scrolled so that is no longer visible,
        // it can be used for one of the new Views appearing. This reused View is the convertView.
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. Only inflate a new View when the convertView supplied
        // by ListView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_search_list_row, parent, false);
            // Creates a ViewHolder and store references to the children views that will data bind to
            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.search_name_tv);
            viewHolder.viewSwitcher = (ViewSwitcher) convertView.findViewById(R.id.amount_search_swchr);
            viewHolder.textView_amount = (TextView) viewHolder.viewSwitcher.findViewById(R.id.search_amount_tv);
            viewHolder.editView_newamount = (EditText) viewHolder.viewSwitcher.findViewById(R.id.hidden_search_amount_ev);
            viewHolder.edit_btn = (Button) convertView.findViewById(R.id.card_search_edit_btn);
            viewHolder.add_btn = (Button) convertView.findViewById(R.id.card_search_add_btn);

            convertView.setTag(viewHolder);
        } else {
            // Get the ViewHolder back to get fast access to the views
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // When one or more elements of the list are edited, (ie. the SwitcherView of this items are
        // in edit mode) and we scroll list, we causing that the view will be reused for other elements.
        // Must provide that views along the list won't copy SwitcherView in edit mode.
        // And when return to the edited element it will be on edit mode.
        // Need to dialog_check each item status (isEdited - edit property saved in object Product) and is view is in
        // edit mode or not (inEditorMode)
        boolean inEditorMode = viewHolder.viewSwitcher.getCurrentView().onCheckIsTextEditor();
        boolean isEdited = prod.isEdited();
        // if view is in edit mode but item was not edited, show textview
        if (inEditorMode && !isEdited) {
            viewHolder.viewSwitcher.showPrevious();
        }
        // if item was edited but view is not in edit mode, show editview
        if (isEdited && !inEditorMode) {
            viewHolder.viewSwitcher.showNext();
        }

        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amount = getNewProductAmount(viewHolder.viewSwitcher);
                if (amount != -1) {
                    prod.amount = amount;
                    prod.setEdited(false);
                    notifyDataSetChanged();
                } else {
                    prod.setEdited(true);
                }
            }
        });

        viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prod.amount != 0) {
                    AddedProductDataSource dataSource = new AddedProductDataSource(mContext);
                    dataSource.open();
                    dataSource.createSimpleAddedProduct(prod);
                    dataSource.close();
                    // reset amount value
                    prod.amount = 0;
                    Toast.makeText(mContext, prod.name + " added", Toast.LENGTH_SHORT).show();
                    CalculateSummary.warningIfCalorieLimitExceeded(mContext);
                } else {
                    Toast.makeText(mContext, "No amount added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Bind the data efficiently with the holder
        viewHolder.textView_name.setText(prod.name);
        String amount = prod.amount + " g";
        viewHolder.textView_amount.setText(amount);

        // set focus for all edit fields in list and show numeric keyboard
        viewHolder.editView_newamount.requestFocus();

        return convertView;
    }

    //http://stackoverflow.com/questions/15194835/filtering-custom-adapter-indexoutofboundsexception
    @Override
    public int getCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    @Override
    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }

    private double getNewProductAmount(ViewSwitcher switcher) {

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_search_amount_ev);
        String newAmount;

        switcher.showNext();

        // if active view is edit view get new amount of product
        if (switcher.getCurrentView().onCheckIsTextEditor()) {
            viewHolder.editView_newamount.setEnabled(true);
            viewHolder.editView_newamount.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(viewHolder.editView_newamount, InputMethodManager.SHOW_IMPLICIT);
        } else {
            newAmount = viewHolder.editView_newamount.getText().toString();
            if (newAmount.isEmpty()) {
                return -1;
            } else {
                viewHolder.editView_newamount.getText().clear();
                return Double.valueOf(newAmount);
            }
        }
        return -1;
    }

    private static class ViewHolder {
        ViewSwitcher viewSwitcher;
        TextView textView_name;
        TextView textView_amount;
        EditText editView_newamount;
        Button edit_btn;
        Button add_btn;
    }
}