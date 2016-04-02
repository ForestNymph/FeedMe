package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class ShowAllSentFoodListActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY_MILLIS = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showall_sent);
        ListView listView = (ListView) findViewById(R.id.showall_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ShowAllSentFoodListAdapter mShowAllSentFoodListAdapter = new ShowAllSentFoodListAdapter(this);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mShowAllSentFoodListAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(alphaInAnimationAdapter);
    }
}
