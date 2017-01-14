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

import java.util.ArrayList;

import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.DailyRecap;
import pl.grudowska.feedme.utils.DatabaseManager;

public class ArchivedActivity extends AppCompatActivity implements OnDismissCallback, DeleteDialogFragment.OnClearItemsCommandListener {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private ArchivedArrayAdapter mArchivedArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archived);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_recently);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArchivedArrayAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.delete_nothing, Toast.LENGTH_LONG).show();
                } else {
                    DeleteDialogFragment delete = new DeleteDialogFragment();
                    delete.show(getFragmentManager(), "");
                }
            }
        });
        final ArrayList<DailyRecap> dailyRecaps = DatabaseManager.getAllDailyRecapsDB(getApplicationContext());
        mArchivedArrayAdapter = new ArchivedArrayAdapter(this, dailyRecaps);
        ListView listView = (ListView) findViewById(R.id.archived_listview);
        assert listView != null;
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mArchivedArrayAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {

            // remove dailyRecap from arrayadapter only
            final DailyRecap dailyRecap = mArchivedArrayAdapter.remove(position);

            Snackbar.make(listView, "Product removed", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {

                    switch (event) {
                        // Action UNDO clicked, dailyRecap restored
                        case Snackbar.Callback.DISMISS_EVENT_ACTION: {
                            mArchivedArrayAdapter.add(position, dailyRecap);
                            break;
                        }
                        // Non action taken or dismiss swiped or new snackbar shown
                        // dailyRecap with connected data removed from db
                        case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                        case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE: {
                            ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
                            dataSource.open();
                            dataSource.deleteArchivedProductsByDate(dailyRecap);
                            dataSource.deleteDailyRecap(dailyRecap);
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

    // Callback from DeleteDialogFragment
    @Override
    public void onClearItemsCommand() {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
        dataSource.open();
        if (dataSource.getAllArchivedDailyRecaps().size() != 0) {
            dataSource.deleteAllArchivedItems();
            mArchivedArrayAdapter.clear();
        }
        dataSource.close();
    }
}
