package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.List;

import pl.grudowska.feedme.data.ExampleDataLoader;
import pl.grudowska.feedme.data.ProductsDataLoader;
import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class MainFoodTypeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private MainFoodTypeListItemAdapter mFoodCardsAdapter;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_food_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton summary_fab = (FloatingActionButton) findViewById(R.id.fab_summary);
        assert summary_fab != null;
        summary_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SummaryDialogFragment summary = new SummaryDialogFragment();
                summary.show(getFragmentManager(), "");
            }
        });

        FloatingActionButton refresh_fab = (FloatingActionButton) findViewById(R.id.fab_refresh);
        assert refresh_fab != null;
        refresh_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ProductsDataLoader.inflateProductType(getApplicationContext());
                ExampleDataLoader.inflateProductType(getApplicationContext());
                mFoodCardsAdapter.refreshDataSource();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                View v = mNavigationView.getHeaderView(0);
                TextView email = (TextView) v.findViewById(R.id.drawer_email_textview);
                email.setText(SharedPreferencesManager.loadDataString(getApplicationContext(), "mailTo", "test@test.pl"));
            }
        };
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // open drawer on start
        // drawer.openDrawer(Gravity.LEFT);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);

        ListView listView = (ListView) findViewById(R.id.activity_main_listview);
        mFoodCardsAdapter = new MainFoodTypeListItemAdapter(this);

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(mFoodCardsAdapter);
        assert listView != null;
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position,
                                    long id) {
                List<String> titles = ProductsDataLoader.getTypeTitles(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), SpecificFoodTypeActivity.class);
                intent.putExtra("FoodType", titles.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right_dots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AboutDialogFragment about = new AboutDialogFragment();
            about.show(getFragmentManager(), "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_summary) {
            Intent intent = new Intent(this, RecentlyAddedFoodActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_showall) {
            Intent intent = new Intent(this, ArchivedListsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_email) {
            EmailDialogFragment dialog = new EmailDialogFragment();
            dialog.show(getFragmentManager(), "");
        } else if (id == R.id.nav_limit) {
            LimitDialogFragment dialog = new LimitDialogFragment();
            dialog.show(getFragmentManager(), "");
        } else if (id == R.id.nav_time) {
            TimeDialogFragment dialog = new TimeDialogFragment();
            dialog.show(getFragmentManager(), "");
        } else if (id == R.id.custom_meals) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
