package pl.grudowska.feedme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.Product;


class AddedFoodArrayAdapter extends ArrayAdapter<Product> implements UndoAdapter {

    private final Context mContext;
    final private OnEditItemListener mListener;

    AddedFoodArrayAdapter(final Context context, ArrayList<Product> added) {
        super(added);
        mContext = context;
        mListener = (OnEditItemListener) mContext;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;
        final Product prod = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_added_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.added_name_tv);
            viewHolder.textView_kcal = (TextView) convertView.findViewById(R.id.added_kcal_tv);
            viewHolder.viewSwitcher = (ViewSwitcher) convertView.findViewById(R.id.amount_swchr);
            viewHolder.textView_amount = (TextView) viewHolder.viewSwitcher.findViewById(R.id.added_amount_tv);
            viewHolder.editView_newamount = (EditText) viewHolder.viewSwitcher.findViewById(R.id.hidden_amount_ev);
            viewHolder.edit_btn = (Button) convertView.findViewById(R.id.card_added_edit_btn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // When one or more elements of the list are edited, (ie. the SwitcherView of this items are
        // in edit mode) and we scroll list, we causing that the view will be reused for other elements.
        // Must provide that views along the list won't copy SwitcherView in edit mode.
        // And when return to the edited element it will be on edit mode.
        // Need to check each item status (isEdited - edit property saved in object Product) and is view is in
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
                    AddedProductDataSource dataSource = new AddedProductDataSource(mContext);
                    dataSource.open();
                    // change value in database
                    dataSource.editAmountOfAddedProduct(prod, amount);
                    // after edit amount update product in array adapter
                    remove(position);
                    add(position, dataSource.getProductByName(prod.name));
                    // close database
                    dataSource.close();
                    prod.setEdited(false);
                    // update total kcal textview after editing product (callback to activity)
                    mListener.onEditItem();
                    // chcek if calories limit exceeded
                    CalculateSummary.warningIfCalorieLimitExceeded(mContext);
                } else {
                    prod.setEdited(true);
                }
            }
        });
        // Bind the data efficiently with the holder
        viewHolder.textView_name.setText(prod.name);
        String kcal = prod.getKcalRelatedWithAmount() + " kcal";
        viewHolder.textView_kcal.setText(kcal);
        String amount = prod.amount + " g";
        viewHolder.textView_amount.setText(amount);

        return convertView;
    }

    private double getNewProductAmount(ViewSwitcher switcher) {

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.editView_newamount = (EditText) switcher.findViewById(R.id.hidden_amount_ev);
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

    @NonNull
    @Override
    public View getUndoView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.undo_row, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_row_undobutton);
    }

    // Callback to communicate with activity when product amount is edited
    // and total kcal should be updated
    interface OnEditItemListener {
        void onEditItem();
    }

    private static class ViewHolder {
        ViewSwitcher viewSwitcher;
        TextView textView_name;
        TextView textView_kcal;
        TextView textView_amount;
        EditText editView_newamount;
        Button edit_btn;
    }
}
