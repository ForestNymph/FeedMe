package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class SpecificFoodTypeActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY_MILLIS = 400;
    private SpecificFoodTypeListItemAdapter mSpecificFoodTypeListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_specific_food_type_scrolling);
        ListView listView = (ListView) findViewById(R.id.activity_specific_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String value = getResources().getString(R.string.app_name);
        if (extras != null) {
            value = extras.getString("FoodType");
        }

        CollapsingToolbarLayout toolbarView =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarView.setTitle(value);
        toolbarView.setBackgroundResource(R.drawable.bread_small);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You can eat today XXX more calories", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSpecificFoodTypeListItemAdapter = new SpecificFoodTypeListItemAdapter(this);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mSpecificFoodTypeListItemAdapter);
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(alphaInAnimationAdapter);
    }
}
