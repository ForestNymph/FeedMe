package pl.grudowska.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import pl.grudowska.feedme.databases.Product;
import pl.grudowska.feedme.utils.SearchEngine;

public class SearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private SearchViewArrayAdapter mSearchAdapter;
    private SearchEngine mEngine;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_added);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddedFoodActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // setup adapter
        mSearchAdapter = new SearchViewArrayAdapter(this, new ArrayList<Product>());
        mListView = (ListView) findViewById(R.id.search_list);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        assert null != searchView;
        searchView.setOnQueryTextListener(this);

        // init search engine
        mEngine = new SearchEngine(getApplicationContext());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayProducts(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.isEmpty()) {
            // clear adapter when no search
            mSearchAdapter.clear();
        } else {
            displayProducts(query);
        }
        return false;
    }

    private void displayProducts(String query) {
        mSearchAdapter.updateProductsList(mEngine.search(query));
        mListView.setAdapter(mSearchAdapter);
    }
}
