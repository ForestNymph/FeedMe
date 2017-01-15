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
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.grudowska.feedme.alghoritms.CalculateSummary;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.DatabaseManager;
import pl.grudowska.feedme.utils.EmailManager;

public class AddedFoodActivity extends AppCompatActivity
        implements OnDismissCallback,
        DeleteDialogFragment.OnClearItemsCommandListener,
        AddedFoodArrayAdapter.OnEditItemListener {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private AddedFoodArrayAdapter mAddedFoodAdapter;

    private ClearAdapterBroadcastReceiver mClearAdapterReceiver = null;
    private boolean isReceiverRegistered = false;
    private TextView mTotalKcalTV;
    private int mCurrentTotalKcal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_food);

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
        ListView listView = (ListView) findViewById(R.id.activity_added_listview);
        mAddedFoodAdapter = new AddedFoodArrayAdapter(this, added);
        assert listView != null;
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mAddedFoodAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);

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
    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {

            // remove product only from adapter
            final Product prod = mAddedFoodAdapter.remove(position);
            final int removedProductAmount = prod.getKcalRelatedWithAmount();
            // update total kcal value after removing product from list
            mCurrentTotalKcal -= removedProductAmount;
            String mUpdateTotalKcal = Integer.toString(mCurrentTotalKcal);
            // update textview with new kcal amount
            mTotalKcalTV.setText(mUpdateTotalKcal);

            Snackbar.make(listView, "Product removed", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {

                    switch (event) {
                        // Action UNDO clicked, product restored
                        case Snackbar.Callback.DISMISS_EVENT_ACTION: {
                            // update total kcal value after restored product in adapter
                            mCurrentTotalKcal += removedProductAmount;
                            String current = Integer.toString(mCurrentTotalKcal);
                            // set new value in textview
                            mTotalKcalTV.setText(current);
                            mAddedFoodAdapter.add(position, prod);
                            break;
                        }
                        // Non action taken or dismiss swiped or new snackbar shown
                        // item with connected data removed
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                        case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                        case Snackbar.Callback.DISMISS_EVENT_SWIPE: {
                            AddedProductDataSource dataSource = new AddedProductDataSource(getApplicationContext());
                            dataSource.open();
                            dataSource.deleteAddedProduct(prod);
                            dataSource.close();
                            break;
                        }
                    }
                }
            }).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        }
    }

    @Override
    public void onClearItemsCommand() {
        mTotalKcalTV.setText("0");
        AddedProductDataSource dataSource = new AddedProductDataSource(getApplicationContext());
        dataSource.open();
        if (dataSource.getAllAddedProducts().size() != 0) {
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
        mCurrentTotalKcal = CalculateSummary.getTotalKcal(getApplicationContext());
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

    // If DailySummaryEmailIntentService is on - sends automatically daily summary email and clear database
    // then BroadcastReceiver clears adapter when activity is active and update calories amount
    public class ClearAdapterBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTotalKcalTV.setText("0");
            mAddedFoodAdapter.clear();
        }
    }
}
