package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.databases.ProductDataSource;
import pl.grudowska.feedme.utils.SearchEngine;

public class SearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private ListView mListView;
    private ProductDataSource mProducts;
    private SearchViewArrayAdapter mSearchAdapter;
    private List<Product> mProductsList;
    private SearchEngine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        mProductsList = new ArrayList<>();
        mSearchAdapter = new SearchViewArrayAdapter(this, mProductsList);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mSearchAdapter);
        mListView = (ListView) findViewById(R.id.search_list);
        assert mListView != null;
        alphaInAnimationAdapter.setAbsListView(mListView);
        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        mListView.setAdapter(alphaInAnimationAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        assert null != searchView;
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        mProducts = new ProductDataSource(getApplicationContext());
        try {
            mProducts.openDataBase();
        } catch (ProductDataSource.DatabaseNotExistException e) {
            e.printStackTrace();
        }
        mEngine = new SearchEngine(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProducts != null) {
            mProducts.close();
        }
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayProducts(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            mListView.setAdapter(mSearchAdapter);
        } else {
            displayProducts(newText);
        }
        return false;
    }

    private void displayProducts(String input) {
        mProductsList.clear();
        mProductsList = mEngine.search(input);
        mListView.setAdapter(new SearchViewArrayAdapter(this, mProductsList));
    }
}
