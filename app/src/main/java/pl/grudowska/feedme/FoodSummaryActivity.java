package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.util.Arrays;

public class FoodSummaryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private FoodSummaryListItemAdapter mFoodSummaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarView =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarView.setTitle("Summary");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.activity_summary_listview);
        mFoodSummaryAdapter = new FoodSummaryListItemAdapter(this);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mFoodSummaryAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position,
                                    long id) {
                Log.d("############", "Items " + "" + position);
                Intent intent = new Intent(getApplicationContext(), SpecificFoodTypeActivity.class);
                intent.putExtra("FoodType", Arrays.asList(getResources()
                        .getStringArray(R.array.typefood_array)).get(position));
                startActivity(intent);
            }
        });

        //
        for (int i = 0; i < 10; i++) {
            mFoodSummaryAdapter.add(i);
        }
    }

    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mFoodSummaryAdapter.remove(position);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
