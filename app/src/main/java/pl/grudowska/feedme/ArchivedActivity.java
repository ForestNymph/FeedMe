package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.util.ArrayList;

import pl.grudowska.feedme.databases.ArchivedProductDataSource;
import pl.grudowska.feedme.databases.DailyRecap;
import pl.grudowska.feedme.utils.DatabaseManager;

public class ArchivedActivity extends AppCompatActivity
        implements DeleteDialogFragment.OnClearItemsCommandListener {

    private ArchivedArrayAdapter mArchivedArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archived);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_archived);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_delete);
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
        DynamicListView listView = (DynamicListView) findViewById(R.id.archived_listview);
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(mArchivedArrayAdapter,
                this, new ArchivedActivity.RemoveOnDismissCallback(mArchivedArrayAdapter));

        assert listView != null;
        listView.setAdapter(swipeUndoAdapter);
        listView.enableSimpleSwipeUndo();

    }

    // Callback from DeleteDialogFragment
    @Override
    public void onClearItemsCommand() {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
        dataSource.open();
        if (!dataSource.getAllArchivedDailyRecaps().isEmpty()) {
            dataSource.deleteAllArchivedItems();
            mArchivedArrayAdapter.clear();
        }
        dataSource.close();
    }

    private class RemoveOnDismissCallback implements OnDismissCallback {

        private final ArrayAdapter<DailyRecap> mAdapter;

        RemoveOnDismissCallback(final ArrayAdapter<DailyRecap> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
            ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
            dataSource.open();
            DailyRecap dailyRecap;
            for (int position : reverseSortedPositions) {
                dailyRecap = mAdapter.remove(position);
                dataSource.deleteArchivedProductsByDate(dailyRecap);
                dataSource.deleteDailyRecap(dailyRecap);
            }
            dataSource.close();
        }
    }
}
