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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import pl.grudowska.feedme.data.AdditionalsDataLoader;
import pl.grudowska.feedme.databases.ProductDataBase;
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
                AdditionalsDataLoader.inflateProductType(getApplicationContext());
                // Update dataset with types
                if (mFoodCardsAdapter != null) {
                    mFoodCardsAdapter.clear();
                    mFoodCardsAdapter.updateDataSet();
                }
                // Update products database
                new DownloadDatabaseTask().execute(ProductDataBase.DATABASE_ADDRESS + ProductDataBase.DATABASE_NAME);
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

        // when app is starting first time load all data from db
        if (mFoodCardsAdapter.getCount() == 0) {
            AdditionalsDataLoader.inflateProductType(getApplicationContext());
            mFoodCardsAdapter.updateDataSet();
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public enum StatusCode {
        SUCCESS, FILE_NOT_FOUND, SERVER_DOWN, FAIL
    }

    private class DownloadDatabaseTask extends AsyncTask<String, Void, StatusCode> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getApplicationContext().deleteDatabase(ProductDataBase.DATABASE_NAME);
        }

        @Override
        protected StatusCode doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection connection;
            InputStream input;
            try {
                assert url != null;
                connection = url.openConnection();
                connection.connect();
                input = new BufferedInputStream(url.openStream(), ProductDataBase.DATABASE_ADDRESS_PORT);

                String databasePath = getApplicationContext().getDatabasePath(ProductDataBase.DATABASE_NAME).toString();

                OutputStream output = new FileOutputStream(databasePath);
                byte data[] = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (ConnectException e) {
                e.printStackTrace();
                return StatusCode.SERVER_DOWN;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return StatusCode.FILE_NOT_FOUND;
            } catch (IOException e) {
                e.printStackTrace();
                return StatusCode.FAIL;
            }
            return StatusCode.SUCCESS;
        }

        @Override
        protected void onPostExecute(StatusCode status) {
            super.onPostExecute(status);

            switch (status) {
                case SUCCESS: {
                    Toast.makeText(getApplicationContext(), "Database has been successfully updated", Toast.LENGTH_LONG).show();
                    break;
                }
                case FILE_NOT_FOUND: {
                    Toast.makeText(getApplicationContext(), "Missing file on the server", Toast.LENGTH_LONG).show();
                    break;
                }
                case SERVER_DOWN: {
                    Toast.makeText(getApplicationContext(), "Server is down", Toast.LENGTH_LONG).show();
                    break;
                }
                case FAIL: {
                    Toast.makeText(getApplicationContext(), "While update an error occurred", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }
}



