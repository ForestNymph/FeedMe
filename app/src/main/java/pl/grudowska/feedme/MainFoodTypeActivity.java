package pl.grudowska.feedme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

import pl.grudowska.feedme.data.AdditionalsDataLoader;
import pl.grudowska.feedme.databases.AddedProductDataSource;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.SharedPreferencesManager;
import pl.grudowska.feedme.utils.StatusCode;

public class MainFoodTypeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private MainFoodTypeArrayAdapter mFoodCardsAdapter;
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

        FloatingActionButton recently_fab = (FloatingActionButton) findViewById(R.id.fab_recently);
        assert recently_fab != null;
        recently_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecentlyAddedFoodActivity.class);
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

        // Send to server database with added products
        FloatingActionButton send_db_fab = (FloatingActionButton) findViewById(R.id.fab_refresh);
        assert send_db_fab != null;
        send_db_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SyncServerAsyncTask().execute(ProductDataSource.getDatabaseAdress(getApplicationContext())
                        + AddedProductDataSource.DATABASE_PATH);
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
        mFoodCardsAdapter = new MainFoodTypeArrayAdapter(this);

        // when app is starting first time load all data from db
        if (mFoodCardsAdapter.getCount() == 0) {
            AdditionalsDataLoader.inflateProductType(getApplicationContext());
            AdditionalsDataLoader.inflateProductSummary(getApplicationContext());
            mFoodCardsAdapter.createDatabase();
        }

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter =
                new SwingBottomInAnimationAdapter(mFoodCardsAdapter);
        assert listView != null;
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recently) {
            Intent intent = new Intent(this, RecentlyAddedFoodActivity.class);
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

    private class SyncServerAsyncTask extends AsyncTask<String, Void, StatusCode> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Checking connection with server...wait", Toast.LENGTH_LONG).show();
        }

        @Override
        protected StatusCode doInBackground(String... urls) {
            // TODO: prepare full file wrapper to send added database using httpUrlConnection class
            final Socket socket;
            try {
                socket = new Socket("http://192.168.1.144:8090/files/", 8090);
                final BufferedOutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
                final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream("added.db"));
                final byte[] buffer = new byte[4096];
                for (int read = inStream.read(buffer); read >= 0; read = inStream.read(buffer))
                    outStream.write(buffer, 0, read);
                inStream.close();
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                return StatusCode.SEND_DATABASE;
            } catch (ConnectException e) {
                e.printStackTrace();
                return StatusCode.SERVER_DOWN;
            } catch (IOException e) {
                e.printStackTrace();
                return StatusCode.FAIL;
            } catch (Exception e) {
                e.printStackTrace();
                return StatusCode.OTHER;
            }
            return StatusCode.SYNC_SUCCESS;
        }

        @Override
        protected void onPostExecute(StatusCode status) {
            super.onPostExecute(status);

            StatusCode.showStatus(getApplicationContext(), status);
        }
    }
}