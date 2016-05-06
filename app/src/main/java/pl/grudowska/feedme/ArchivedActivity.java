package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import pl.grudowska.feedme.databases.ArchivedProductDataSource;

public class ArchivedActivity extends AppCompatActivity implements DeleteDialogFragment.OnClearItemsCommandListener {

    private static final int INITIAL_DELAY_MILLIS = 400;
    private ArchivedArrayAdapter mArchivedArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archived);
        ListView listView = (ListView) findViewById(R.id.archived_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mArchivedArrayAdapter = new ArchivedArrayAdapter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mArchivedArrayAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);
        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(alphaInAnimationAdapter);
    }

    // Callback from DeleteDialogFragment
    @Override
    public void onClearItemsCommand() {
        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getApplicationContext());
        dataSource.open();
        if (dataSource.getAllArchivedDailyRecaps().size() == 0) {
            // do nothing
        } else {
            dataSource.deleteAllArchivedItems();
            mArchivedArrayAdapter.clear();
        }
        dataSource.close();
    }
}
