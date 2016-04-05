package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class AllSentFoodActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY_MILLIS = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showall_sent);
        ListView listView = (ListView) findViewById(R.id.showall_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AllSentFoodListItemAdapter mAllSentFoodListItemAdapter = new AllSentFoodListItemAdapter(this);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mAllSentFoodListItemAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(alphaInAnimationAdapter);
    }
}
