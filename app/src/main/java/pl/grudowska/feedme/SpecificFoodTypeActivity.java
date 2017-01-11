package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;

public class SpecificFoodTypeActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY_MILLIS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_specific_food_type);
        ListView listView = (ListView) findViewById(R.id.specific_listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        String type_value = "";
        if (extras != null) {
            type_value = extras.getString("FoodType");
        }
        assert toolbar != null;
        toolbar.setCollapsible(false);
        toolbar.setTitle(type_value);
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

        SpecificFoodTypeArrayAdapter specificFoodTypeArrayAdapter
                = new SpecificFoodTypeArrayAdapter(this, getDataByType(type_value));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(specificFoodTypeArrayAdapter);
        assert listView != null;
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(alphaInAnimationAdapter);
        listView.setAdapter(specificFoodTypeArrayAdapter);
    }

    private ArrayList<Product> getDataByType(String type) {
        ArrayList<Product> products = new ArrayList<>();
        ProductDataSource dataSource = new ProductDataSource(this);
        try {
            dataSource.openDataBase();
            products = dataSource.getProductsByType(type);
            dataSource.close();

        } catch (ProductDataSource.DatabaseNotExistException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Database needs to be upadated", Toast.LENGTH_SHORT).show();
        }
        return products;
    }
}