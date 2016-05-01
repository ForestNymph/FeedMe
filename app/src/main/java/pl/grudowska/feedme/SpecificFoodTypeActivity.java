package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
        setSupportActionBar(toolbar);

        Button done = (Button) findViewById(R.id.button_done);
        assert done != null;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        String value = getResources().getString(R.string.app_name);
        if (extras != null) {
            value = extras.getString("FoodType");
        }

        CollapsingToolbarLayout toolbarView =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert toolbarView != null;
        toolbarView.setTitle(value);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecentlyAddedFoodActivity.class);
                startActivity(intent);
                finish();
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