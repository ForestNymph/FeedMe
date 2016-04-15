package pl.grudowska.feedme;

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

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.ArchivedListFormatterManager;
import pl.grudowska.feedme.utils.EmailManager;

public class RecentlyAddedFoodActivity extends AppCompatActivity implements OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private int mSavedRowData;
    private RecentlyAddedFoodListItemAdapter mFoodSummaryAdapter;
    private ProductDataSource mAddedProductsDataSource;

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

        FloatingActionButton fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        assert fab_send != null;
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open DB's
                mAddedProductsDataSource = new ProductDataSource(getApplicationContext());
                mAddedProductsDataSource.open();

                String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
                String content = ArchivedListFormatterManager.createMailContent(getApplicationContext());
                // If recently added product list is empty do nothing
                if (mAddedProductsDataSource.getAllAddedProducts().size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.sent_nothing, Toast.LENGTH_SHORT).show();
                    // do nothing
                } else {
                    sendDailySummaryEmail(date, content);
                    Toast.makeText(getApplicationContext(), R.string.sent_message, Toast.LENGTH_SHORT).show();
                }
                // Close DB's
                mAddedProductsDataSource.close();
            }
        });

        ListView listView = (ListView) findViewById(R.id.activity_summary_listview);
        mFoodSummaryAdapter = new RecentlyAddedFoodListItemAdapter(this);
        assert listView != null;
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mFoodSummaryAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);
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
        mAddedProductsDataSource = new ProductDataSource(this);
        mAddedProductsDataSource.open();
        List<Product> values = mAddedProductsDataSource.getAllAddedProducts();
        mAddedProductsDataSource.deleteAddedProduct(values.get(position));
        mAddedProductsDataSource.close();
    }

    private void sendDailySummaryEmail(String date, String content) {
        new EmailManager(getApplicationContext(), date, content);
    }
}
