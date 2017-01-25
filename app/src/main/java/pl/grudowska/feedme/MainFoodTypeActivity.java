package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.List;

import pl.grudowska.feedme.data.AdditionalsDataLoader;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.DownloadDatabaseTask;
import pl.grudowska.feedme.utils.SharedPreferencesManager;

public class MainFoodTypeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // private static final int INITIAL_DELAY_MILLIS = 300;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
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

        FloatingActionButton added_fab = (FloatingActionButton) findViewById(R.id.fab_added);
        assert added_fab != null;
        added_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddedFoodActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton search_fab = (FloatingActionButton) findViewById(R.id.fab_search);
        assert search_fab != null;
        search_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchViewActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton refresh_db_fab = (FloatingActionButton) findViewById(R.id.fab_refresh);
        assert refresh_db_fab != null;
        refresh_db_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update products database
                new DownloadDatabaseTask(getApplicationContext()).execute(ProductDataSource.getDatabaseAdress(getApplicationContext())
                        + ProductDataSource.getDatabaseName(getApplicationContext()));
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // open drawer on start
        // drawer.openDrawer(Gravity.LEFT);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);

        ListView listView = (ListView) findViewById(R.id.activity_main_listview);
        MainFoodTypeArrayAdapter mFoodCardsAdapter = new MainFoodTypeArrayAdapter(this);

        // when app is starting first time load all data to db
        if (mFoodCardsAdapter.getCount() == 0) {
            AdditionalsDataLoader.inflateProductsType(getApplicationContext());
            AdditionalsDataLoader.inflateProducsRanges(getApplicationContext());
            mFoodCardsAdapter.createDatabase();
        }

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(mFoodCardsAdapter);
        assert listView != null;
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().disableAnimations();
        listView.setAdapter(swingBottomInAnimationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                List<String> titles = AdditionalsDataLoader.getTypeTitles(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), SpecificFoodTypeActivity.class);
                intent.putExtra("FoodType", titles.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_added) {
            Intent intent = new Intent(this, AddedFoodActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_archived) {
            Intent intent = new Intent(this, ArchivedActivity.class);
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
        } else if (id == R.id.nav_server) {
            ServerDialogFragment dialog = new ServerDialogFragment();
            dialog.show(getFragmentManager(), "");
        } else if (id == R.id.nav_about) {
            AboutDialogFragment dialog = new AboutDialogFragment();
            dialog.show(getFragmentManager(), "");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}