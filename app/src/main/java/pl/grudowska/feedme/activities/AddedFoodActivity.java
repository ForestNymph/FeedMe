package pl.grudowska.feedme.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.grudowska.feedme.R;
import pl.grudowska.feedme.adapters.AddedFoodArrayAdapter;
import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.dialogFragments.DeleteDialogFragment;
import pl.grudowska.feedme.dialogFragments.SummaryDialogFragment;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.EmailManager;

public class AddedFoodActivity extends AppCompatActivity
        implements DeleteDialogFragment.OnClearItemsCommandListener,
        AddedFoodArrayAdapter.OnEditItemListener {

    private AddedFoodArrayAdapter mAddedFoodAdapter;

    private ClearAdapterBroadcastReceiver mClearAdapterReceiver = null;
    private boolean isReceiverRegistered = false;
    private TextView mTotalKcalTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_food);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_added);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_summary = (FloatingActionButton) findViewById(R.id.fab_summary);
        assert fab_summary != null;
        fab_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SummaryDialogFragment summary = new SummaryDialogFragment();
                summary.show(getFragmentManager(), "");
            }
        });

        FloatingActionButton search_fab = (FloatingActionButton) findViewById(R.id.fab_search);
        assert search_fab != null;
        search_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        assert fab_delete != null;
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddedFoodAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.delete_nothing, Toast.LENGTH_LONG).show();
                } else {
                    DeleteDialogFragment delete = new DeleteDialogFragment();
                    delete.show(getFragmentManager(), "");
                }
            }
        });

        FloatingActionButton fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        assert fab_send != null;
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If recently added product list is empty do nothing
                if (mAddedFoodAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.sent_nothing, Toast.LENGTH_LONG).show();
                } else {
                    new SendAction().execute();
                }
            }
        });

        final ArrayList<Product> added = DatabaseManager.getAllAddedProductsDB(getApplicationContext());
        DynamicListView listView = (DynamicListView) findViewById(R.id.activity_added_listview);

        mAddedFoodAdapter = new AddedFoodArrayAdapter(this, added);
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(mAddedFoodAdapter,
                this, new RemoveOnDismissCallback(mAddedFoodAdapter));

        assert listView != null;
        listView.setAdapter(swipeUndoAdapter);
        listView.enableSimpleSwipeUndo();

        mTotalKcalTV = (TextView) findViewById(R.id.total_amount_tv);
        if (mTotalKcalTV != null) {
            updateTotalKcalTextView();
        }
        mClearAdapterReceiver = new ClearAdapterBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isReceiverRegistered) {
            registerReceiver(mClearAdapterReceiver, new IntentFilter("AddedFoodActivity.clearAdapter"));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isReceiverRegistered) {
            unregisterReceiver(mClearAdapterReceiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    public void onClearItemsCommand() {
        mTotalKcalTV.setText("0");
        AddedProductDataSource dataSource = new AddedProductDataSource(getApplicationContext());
        dataSource.open();
        if (!dataSource.getAllAddedProducts().isEmpty()) {
            mAddedFoodAdapter.clear();
            dataSource.clearAll();
        }
        dataSource.close();
    }

    @Override
    public void onEditItem() {
        updateTotalKcalTextView();
    }

    private void updateTotalKcalTextView() {
        int mCurrentTotalKcal = CalculateSummary.getTotalKcal(getApplicationContext());
        String totalStr = Integer.toString(mCurrentTotalKcal);
        mTotalKcalTV.setText(totalStr);
    }

    private class SendAction extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), R.string.prepare_message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            String contentMail = ArchivedListFormatterManager.createMailContent(getApplicationContext());
            int totalKcal = CalculateSummary.getTotalKcal(getApplicationContext());

            new EmailManager(getApplicationContext(), date, contentMail);

            ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
            dataSource.open();
            List<Product> products = DatabaseManager.getAllAddedProductsDB(getApplicationContext());
            for (int i = 0; i < products.size(); ++i) {
                dataSource.createArchivedProduct(date, products.get(i));
            }
            dataSource.createDailyRecap(date, totalKcal, contentMail);
            dataSource.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), R.string.sent_message, Toast.LENGTH_SHORT).show();
        }
    }

    // If DailySummaryEmailIntentService is on - sends automatically daily dialog_summary dialog_email and clear database
    // then BroadcastReceiver clears adapter when activity is active and update calories amount
    public class ClearAdapterBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTotalKcalTV.setText("0");
            mAddedFoodAdapter.clear();
        }
    }

    private class RemoveOnDismissCallback implements OnDismissCallback {

        private final ArrayAdapter<Product> mAdapter;

        RemoveOnDismissCallback(final ArrayAdapter<Product> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
            AddedProductDataSource dataSource = new AddedProductDataSource(getApplicationContext());
            dataSource.open();
            Product prod;
            for (int position : reverseSortedPositions) {
                prod = mAdapter.remove(position);
                dataSource.deleteAddedProduct(prod);
            }
            dataSource.close();
            updateTotalKcalTextView();
        }
    }
}
