package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class SummaryFoodActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private SummaryFoodListItemAdapter mFoodSummaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Count food...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.activity_summary_listview);
        mFoodSummaryAdapter = new SummaryFoodListItemAdapter(this);
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

        for (int i = 0; i < 30; i++) {
            mFoodSummaryAdapter.add(i);
        }
    }

    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {

            Snackbar.make(listView, "Product removed", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    switch (event) {
                        case Snackbar.Callback.DISMISS_EVENT_ACTION:
                            // Action UNDO clicked
                            // TODO: refresh with swiped position
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                            // Action UNDO timeout
                            mFoodSummaryAdapter.remove(position);
                            break;
                    }
                }

                @Override
                public void onShown(Snackbar snackbar) {
                    //Toast.makeText(getApplicationContext(), "This is my annoying step-brother", Toast.LENGTH_LONG).show();
                }
            }).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: refresh with swiped position
                }
            }).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
