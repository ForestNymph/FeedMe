package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class ArchivedListsActivity extends AppCompatActivity implements DeleteDialogFragment.OnClearItemsCommandListener {

    private static final int INITIAL_DELAY_MILLIS = 400;
    private ArchivedListItemAdapter mArchivedListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archived);
        ListView listView = (ListView) findViewById(R.id.showall_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mArchivedListItemAdapter = new ArchivedListItemAdapter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArchivedListItemAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.delete_nothing, Toast.LENGTH_LONG).show();
                } else {
                    DeleteDialogFragment delete = new DeleteDialogFragment();
                    delete.show(getFragmentManager(), "");
                }
            }
        });
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mArchivedListItemAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);
        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(alphaInAnimationAdapter);
    }

    // Callback from DeleteDialogFragment
    @Override
    public void onClearItemsCommand() {
        mArchivedListItemAdapter.clear();
    }
}
