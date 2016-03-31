package pl.grudowska.feedme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

public class SummaryFoodActivity extends AppCompatActivity implements OnDismissCallback {

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
                            // Action UNDO clicked, restore product
                            // TODO load copy of lists before item removal
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                            // Action UNDO timeout
                            mFoodSummaryAdapter.remove(position);
                            break;
                    }
                }
            }).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        }
    }
}
