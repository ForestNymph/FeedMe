package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class SpecificFoodTypeActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY_MILLIS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_specific_food_type);
        ListView listView = (ListView) findViewById(R.id.specific_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("FoodType");
        }
        assert toolbar != null;
        toolbar.setCollapsible(false);
        toolbar.setTitle(value);
        // toolbar.setSubtitle(value);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_recently = (FloatingActionButton) findViewById(R.id.fab_recently);

        assert fab_recently != null;
        fab_recently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecentlyAddedFoodActivity.class);
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton fab_close = (FloatingActionButton) findViewById(R.id.fab_close);
        assert fab_close != null;
        fab_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SpecificFoodTypeArrayAdapter specificFoodTypeArrayAdapter = new SpecificFoodTypeArrayAdapter(this, value);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(specificFoodTypeArrayAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(alphaInAnimationAdapter);
        listView.setAdapter(specificFoodTypeArrayAdapter);
    }
}