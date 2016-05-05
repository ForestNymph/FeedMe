package pl.grudowska.feedme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.SupplementaryInfoDataSource;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.EmailManager;

public class RecentlyAddedFoodActivity extends AppCompatActivity implements OnDismissCallback, DeleteDialogFragment.OnClearItemsCommandListener {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private int mSavedRowData;
    private RecentlyAddedFoodArrayAdapter mFoodSummaryAdapter;

    private ClearAdapterBroadcastReceiver mClearAdapterReceiver = null;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentlyadded_food);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        assert fab_delete != null;
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFoodSummaryAdapter.getCount() == 0) {
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
                if (mFoodSummaryAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.sent_nothing, Toast.LENGTH_SHORT).show();
                    // do nothing
                } else {
                    new SendAction().execute();
                }
            }
        });

        ListView listView = (ListView) findViewById(R.id.activity_recently_listview);
        mFoodSummaryAdapter = new RecentlyAddedFoodArrayAdapter(this);
        assert listView != null;
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mFoodSummaryAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);

        mClearAdapterReceiver = new ClearAdapterBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReceiverRegistered) {
            // do nothing
        } else {
            registerReceiver(mClearAdapterReceiver, new IntentFilter("RecentlyAddedFoodActivity.clearAdapter"));
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

    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {

            // in case of undo save row before remove product from adapter
            mSavedRowData = mFoodSummaryAdapter.getItem(position);
            mFoodSummaryAdapter.remove(mFoodSummaryAdapter.getItem(position));

            Snackbar.make(listView, "Product removed", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {

                    switch (event) {
                        case Snackbar.Callback.DISMISS_EVENT_ACTION:
                            // Action UNDO clicked, product restored
                            mFoodSummaryAdapter.add(position, mSavedRowData);
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                            // Action UNDO timeout, product removed
                            removeProductFromDB(position);
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                            // Indicates that the Snackbar was dismissed from a new Snackbar being shown, product removed
                            removeProductFromDB(position);
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                            // Indicates that the Snackbar was dismissed via a swipe, product removed
                            removeProductFromDB(position);
                            break;
                    }
                }
            }).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        }
    }

    private void removeProductFromDB(int position) {
        SupplementaryInfoDataSource addedProductsDataSource = new SupplementaryInfoDataSource(this);
        addedProductsDataSource.open();
        List<Product> values = addedProductsDataSource.getAllAddedProducts();
        addedProductsDataSource.deleteAddedProduct(values.get(position));
        addedProductsDataSource.close();
    }

    @Override
    public void onClearItemsCommand() {
        SupplementaryInfoDataSource dataSource = new SupplementaryInfoDataSource(getApplicationContext());
        dataSource.open();
        if (dataSource.getAllAddedProducts().size() == 0) {
            // do nothing
        } else {
            dataSource.deleteAllAddedProducts();
            mFoodSummaryAdapter.clear();
        }
        dataSource.close();
    }

    private class SendAction extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), R.string.prepare_message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            String contentMail = ArchivedListFormatterManager.createMailContent(getApplicationContext());
            int totalKcal = CalculateSummary.getTotalKcal(getApplicationContext());

            new EmailManager(getApplicationContext(), date, contentMail);

            ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
            dataSource.open();
            List<Product> products = DatabaseManager.getAddedProductsDB(getApplicationContext());
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

    public class ClearAdapterBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mFoodSummaryAdapter.clear();
            mFoodSummaryAdapter.notifyDataSetChanged();
        }
    }
}
